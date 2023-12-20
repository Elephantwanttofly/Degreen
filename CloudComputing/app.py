import os
import numpy as np

from flask import Flask, jsonify, request
import firebase_admin
from firebase_admin import credentials, db
import random
from PIL import Image
from tensorflow.keras.models import load_model
from werkzeug.utils import secure_filename

app = Flask(__name__)
app.config['ALLOWED_EXTENSIONS'] = set(['png', 'jpg', 'jpeg'])
app.config['UPLOAD_FOLDER'] = 'db.prediksi'
app.config['MODEL_FILE'] = 'soil_classification_model2.h5'
app.config['LABELS_FILE'] = 'labels.txt'

cred = credentials.Certificate('degreen-project-capstone-firebase-adminsdk-k7s32-dab5bf5bc6.json')
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://degreen-project-capstone-default-rtdb.asia-southeast1.firebasedatabase.app/'  # Ganti dengan URL database Firebase Anda
})
firebase_ref = db.reference('/predictions')

def allowed_image(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in app.config['ALLOWED_EXTENSIONS']


model = load_model(app.config['MODEL_FILE'], compile=False)
with open(app.config['LABELS_FILE'], 'r') as file:
    labels = file.read().splitlines()

def predict_image(image):
    img = Image.open(image).convert("RGB")
    img = img.resize((224, 224))
    img_array = np.asarray(img)
    img_array = np.expand_dims(img_array, axis=0)
    normalized_image_array = (img_array.astype(np.float32) / 255.0)
    predictions = model.predict(normalized_image_array)
    index = np.argmax(predictions)
    class_name = labels[index]
    confidence_score = predictions[0][index]
    return class_name, confidence_score

@app.route("/")
def index():
    return jsonify({
        "status": {
            "code": 200,
            "message": "success fetching api",
        },
        "data": None
    }), 200

#PLANTS FUNCTION
@app.route('/plants', methods=['GET'])
def get_all_plants():
    valid_id = ['T01P', 'T02D', 'T03M', 'T04J', 'T05S', 'T06B', 'T07K', 'T08T', 'T09T', 'T10B', 'T11S', 'T12A', 'T13J', 'T14K', 'T15W', 'T16B',
                 'T17K', 'T18J', 'T19K', 'T20C', 'T21N', 'T22K', 'T23R', 'T24A', 'T25L', 'T26K', 'T27C', 'T28S', 'T29K', 'T30S', 'T31A', 'T32P',
                 'T33D', 'T34J', 'T35B', 'T36K', 'T37U', 'T38B', 'T39M', 'T40K', 'T41M', 'T42J', 'T43J', 'T44M', 'T45S', 'T46C', 'T47J', 'T48P', 'T49B', 'T50K']

    plant_ref = db.reference('jenis_tanaman')
    all_plants = plant_ref.get()

    if all_plants:
        plant_list = [{"id": key, **value} for key, value in all_plants.items()]
        return jsonify({
            "status": {
                "code": 200,
                "message": "Success get all plants",
            },
            "data": plant_list
        }), 200
    else:
        return jsonify({
            "status": {
                "code": 404,
                "message": "Plants not found",
            }
        }), 404


@app.route('/plants/<string:id_tanaman>', methods=['GET'])
def get_plant_or_all_plants(id_tanaman):
    valid_id = ['T01P', 'T02D', 'T03M', 'T04J', 'T05S', 'T06B', 'T07K', 'T08T', 'T09T', 'T10B', 'T11S', 'T12A', 'T13J', 'T14K', 'T15W', 'T16B',
                 'T17K', 'T18J', 'T19K', 'T20C', 'T21N', 'T22K', 'T23R', 'T24A', 'T25L', 'T26K', 'T27C', 'T28S', 'T29K', 'T30S', 'T31A', 'T32P',
                 'T33D', 'T34J', 'T35B', 'T36K', 'T37U', 'T38B', 'T39M', 'T40K', 'T41M', 'T42J', 'T43J', 'T44M', 'T45S', 'T46C', 'T47J', 'T48P', 'T49B', 'T50K']

    plant_ref = db.reference('jenis_tanaman')

    if not id_tanaman:  # Jika id_tanaman tidak diberikan
        all_plants = plant_ref.get()

        if all_plants:
            return jsonify({
                "status": {
                    "code": 200,
                    "message": "Success get all plants",
                },
                "data": [{"id": key, **value} for key, value in all_plants.items()]
            }), 200
        else:
            return jsonify({
                "status": {
                    "code": 404,
                    "message": "Plants not found",
                }
            }), 404

    elif id_tanaman in valid_id:  # Jika id_tanaman adalah ID yang valid
        plant_data = plant_ref.child(id_tanaman).get()

        if plant_data:
            data_requested = request.args.get('data_requested')

            if data_requested:
                if data_requested in ['deskripsi_tanaman', 'nama', 'url_gambar', 'url_produk']:
                    response_data = plant_data.get(data_requested)
                    return jsonify({
                        "status": {
                            "code": 200,
                            "message": f"Success get {data_requested} based on ID {id_tanaman}",
                        },
                        "data": [response_data]  # Mengembalikan data yang diminta dalam format JSON array
                    }), 200
                else:
                    return jsonify({
                        "status": {
                            "code": 400,
                            "message": "Invalid data_requested parameter"
                        }
                    }), 400

            return jsonify({
                "status": {
                    "code": 200,
                    "message": f"Success get detail based on ID {id_tanaman}",
                },
                "data": [{"id": id_tanaman, **plant_data}]  # Mengembalikan data tanaman dalam bentuk array objek
            }), 200
        else:
            return jsonify({
                "status": {
                    "code": 404,
                    "message": "Plant ID not found",
                }
            }), 404

    else:
        return jsonify({
            "status": {
                "code": 400,
                "message": "Invalid request or Plant ID not found"
            }
        }), 400

#API SOIL TYPE
@app.route('/soil', methods=['GET'])
def get_all_soils():
    valid_ids = ['001', '002', '003', '004', '005']  # Daftar ID tanah yang valid

    soils_ref = db.reference('tanah')
    all_soils = soils_ref.get()

    if all_soils:
        soil_list = [{"id": key, **value} for key, value in all_soils.items() if key in valid_ids]
        return jsonify({
            "status": {
                "code": 200,
                "message": "Success get all soils",
            },
            "data": soil_list
        }), 200
    else:
        return jsonify({
            "status": {
                "code": 404,
                "message": "Soils not found",
            }
        }), 404


@app.route('/soil/<string:id_tanah>', methods=['GET'])
def get_soil_by_id(id_tanah):
    valid_ids = ['001', '002', '003', '004', '005']

    if id_tanah in valid_ids:
        soil_ref = db.reference('tanah').child(id_tanah)
        soil_data = soil_ref.get()

        data_requested = request.args.get('data_requested')
        plant_id = request.args.get('plant_id')
        get_detail = request.args.get('get_detail')

        if data_requested:
            if data_requested == 'rekomendasi_bibit':
                rekomendasi_bibit = soil_data.get('rekomendasi_bibit')
                if rekomendasi_bibit:
                    # Jika ada rekomendasi_bibit tetapi tanpa plant_id yang disediakan
                    if not plant_id:
                        rekomendasi_list = [{"plant_id": key, **value} for key, value in rekomendasi_bibit.items()]
                        return jsonify({
                            "status": {
                                "code": 200,
                                "message": f"Success get rekomendasi bibit for soil {id_tanah}",
                            },
                            "data": rekomendasi_list
                        }), 200
                    else:
                        if plant_id in rekomendasi_bibit:
                            response_data = rekomendasi_bibit[plant_id]

                            if get_detail:
                                detail_data = response_data.get(get_detail)
                                if detail_data:
                                    return jsonify({
                                        "status": {
                                            "code": 200,
                                            "message": f"Success get {get_detail} for plant_id {plant_id} in soil {id_tanah}",
                                        },
                                        "data": [detail_data]  # Mengembalikan data dalam bentuk array objek
                                    }), 200
                                else:
                                    return jsonify({
                                        "status": {
                                            "code": 404,
                                            "message": f"No {get_detail} found for plant_id {plant_id} in soil {id_tanah}"
                                        }
                                    }), 404

                            return jsonify({
                                "status": {
                                    "code": 200,
                                    "message": f"Success get rekomendasi bibit for plant_id {plant_id} in soil {id_tanah}",
                                },
                                "data": [response_data]  # Mengembalikan data dalam bentuk array objek
                            }), 200
                        else:
                            return jsonify({
                                "status": {
                                    "code": 404,
                                    "message": f"No rekomendasi bibit found for plant_id {plant_id} in soil {id_tanah}"
                                }
                            }), 404

                else:
                    return jsonify({
                        "status": {
                            "code": 404,
                            "message": f"No rekomendasi bibit found for soil {id_tanah}"
                        }
                    }), 404

            elif data_requested in ['deskripsi_tanah', 'jenis', 'url_tanah']:
                requested_data = soil_data.get(data_requested)
                if requested_data:
                    return jsonify({
                        "status": {
                            "code": 200,
                            "message": f"Success get {data_requested} for soil {id_tanah}",
                        },
                        "data": [requested_data]  # Mengembalikan data dalam bentuk array objek
                    }), 200
                else:
                    return jsonify({
                        "status": {
                            "code": 404,
                            "message": f"No {data_requested} found for soil {id_tanah}"
                        }
                    }), 404

            else:
                return jsonify({
                    "status": {
                        "code": 400,
                        "message": "Invalid data_requested parameter"
                    }
                }), 400

        return jsonify({
            "status": {
                "code": 200,
                "message": f"Success get detail for soil {id_tanah}",
            },
            "data": [soil_data]  # Mengembalikan data dalam bentuk array objek
        }), 200

    else:
        return jsonify({
            "status": {
                "code": 404,
                "message": "Soil ID not found"
            }
        }), 404



# MAIN FUNCTION
@app.route('/upload', methods=['POST'])
def upload_image():
    uploads_dir = 'uploads' 
    if not os.path.exists(uploads_dir):
        os.makedirs(uploads_dir)

    if 'image' not in request.files:
        return jsonify({
        "status": {
            "code": 400,
            "message": "Bad request: Please input an image"
        }
    }), 400

    image = request.files['image']

    if image.filename == '':
        return jsonify({
        "status": {
            "code": 400,
            "message": "Bad request: No selected image"
        }
    }), 400

    if image and allowed_image(image.filename):
        image_path = os.path.join(uploads_dir, secure_filename(image.filename)) 
        image.save(image_path)

        class_name, confidence_score = predict_image(image_path)

        confidence_percentage = "{:.2%}".format(confidence_score)

        firebase_ref.push({
            'image_name': image.filename,
            'class_name': class_name,
            'confidence_score': confidence_percentage 
        })

        return jsonify({
            'success': 'File uploaded and predictions saved to Firebase',
            'prediction': {
                'class_name': class_name,
                'confidence_score': confidence_percentage 
            }
        })
    else:
        return jsonify({
            "status": {
                "code": 400,
                "message": "Failed to upload file or file type not allowed"
            }
        }), 400

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))