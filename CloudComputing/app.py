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

#PLANTS FUNCTION
@app.route('/plants/<string:id_tanaman>', methods=['GET'])
def get_plant_or_all_plants(id_tanaman):
    valid_id = ['T01P', 'T02D', 'T03M', 'T04J', 'T05S', 'T06B', 'T07K', 'T08T', 'T09T', 'T10B', 'T11S', 'T12A', 'T13J', 'T14K', 'T15W', 'T16B',
                 'T17K', 'T18J', 'T19K', 'T20C', 'T21N', 'T22K', 'T23R', 'T24A', 'T25L', 'T26K', 'T27C', 'T28S', 'T29K', 'T30S', 'T31A', 'T32P',
                 'T33D', 'T34J', 'T35B', 'T36K', 'T37U', 'T38B', 'T39M', 'T40K', 'T41M', 'T42J', 'T43J', 'T44M', 'T45S', 'T46C', 'T47J', 'T48P', 'T49B', 'T50K']

    if id_tanaman == "plants":
        plant_ref = db.reference('jenis_tanaman')
        all_plants = plant_ref.get()

        if all_plants:
            plants_array = [{"id_tanaman": key, "data": value} for key, value in all_plants.items()]

            return jsonify({
                "status": {
                    "code": 200,
                    "message": "Success get all plants",
                },
                "data": plants_array
            }), 200
        else:
            return jsonify({
                "status": {
                    "code": 404,
                    "message": "Plants not found",
                }
            }), 404

    elif id_tanaman in valid_id:
        plant_ref = db.reference('jenis_tanaman').child(id_tanaman)
        plant_data = plant_ref.get()

        data_requested = request.args.get('data_requested')
        if data_requested:
            if data_requested in ['deskripsi_tanaman', 'id_tanaman', 'nama', 'url_gambar', 'url_produk']:
                response_data = plant_data.get(data_requested)
                return jsonify({
                    "status": {
                        "code": 200,
                        "message": f"Success get {data_requested} based on ID {id_tanaman}"
                    },
                    "data": response_data
                }), 200
            else:
                return jsonify({
                    "status": {
                        "code": 400,
                        "message": "Invalid data_requested parameter"
                    }
                }), 400

        plant_array = [{"id_tanaman": id_tanaman, "data": plant_data}]
        return jsonify({
            "status": {
                "code": 200,
                "message": f"Success get detail based on ID {id_tanaman}",
            },
            "data": plant_array
        }), 200
    else:
        return jsonify({
            "status": {
                "code": 400,
                "message": "Invalid request or Plant ID not found"
            }
        }), 400


@app.route('/plants/search', methods=['GET'])
def search_plants_by_keyword():
    keyword = request.args.get('keyword')

    if not keyword:
        return jsonify({
            "status": {
                "code": 400,
                "message": "Bad request, please input keyword for search"
            }
        }), 400

    plants_ref = db.reference('jenis_tanaman')
    all_plants_data = plants_ref.get()

    if all_plants_data:
        matched_plants = {}
        for plant_id, plant_data in all_plants_data.items():
            plant_name = plant_data.get('nama', '').lower()
            if keyword.lower() in plant_name:
                matched_plants[plant_id] = plant_data

        if matched_plants:
            return jsonify({
                "status": {
                    "code": 200,
                    "message": f"Found matching plants for the keyword '{keyword}'",
                },
                "data": matched_plants
            }), 200
        else:
            return jsonify({
                "status": {
                    "code": 404,
                    "message": f"No matching plants found for the keyword '{keyword}'"
                }
            }), 404

# API SOIL TYPE
@app.route('/soil/<string:id_tanah>', methods=['GET'])
def get_soil_by_id(id_tanah):
    valid_ids = ['001', '002', '003', '004', '005']

if id_tanah in valid_ids:
    soil_ref = db.reference('tanah').child(id_tanah)
    soil_data = soil_ref.get()

    data_requested = request.args.get('data_requested')

    if data_requested:
        if data_requested in ['deskripsi_tanah', 'jenis', 'nama_tanah', 'url_tanah', 'rekomendasi_bibit']:
            response_data = soil_data.get(data_requested)
            return jsonify({
                "status": {
                    "code": 200,
                    "message": f"Success get {data_requested} for soil {id_tanah}"
                },
                "data": response_data
            }), 200
        else:
            return jsonify({
                "status": {
                    "code": 400,
                    "message": "Invalid data_requested parameter"
                }
            }), 400

    soil_array = [{"id_tanah": id_tanah, "data": soil_data}]
    return jsonify({
        "status": {
            "code": 200,
            "message": f"Success get detail for soil {id_tanah}",
        },
        "data": soil_array
    }), 200

soils_ref = db.reference('tanah')
all_soils_data = soils_ref.get()

if all_soils_data:
    soils_array = [{"id_tanah": key, "data": value} for key, value in all_soils_data.items()]
    return jsonify({
        "status": {
            "code": 200,
            "message": "Success get all soils",
        },
        "data": soils_array
    }), 200
else:
    return jsonify({
        "status": {
            "code": 404,
            "message": "No soils found"
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

        confidence_percentage = int(float(confidence_score) * 100)

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
