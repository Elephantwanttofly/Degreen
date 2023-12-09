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

cred = credentials.Certificate('C:\\Users\\Lenovo\\AppData\\Local\\Programs\\degreen-project-capstone-firebase-adminsdk-k7s32-dab5bf5bc6.json')
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://degreen-project-capstone-default-rtdb.asia-southeast1.firebasedatabase.app/'  # Ganti dengan URL database Firebase Anda
})
firebase_ref = db.reference('/predictions')

def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1] in app.config['ALLOWED_EXTENSIONS']

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
            "message": "Welcome to DeGreen",
        },
        "data": None
    }), 200

# API TYPES OF PLANTS

@app.route('/plants', methods=['GET'])
def get_all_plants():
    plants_ref = db.reference('jenis_tanaman')
    all_plants_data = plants_ref.get()
    
    if all_plants_data:
        return jsonify({
            "status": {
                "code": 200,
                "message": "Success get all plants",
            },
            "data": all_plants_data
        }), 200
    else:
        return jsonify({
            "status": {
                "code": 404,
                "message": "No plants found"
            }
        }), 404

@app.route('/plants/<string:plant_id>', methods=['GET'])
def plant_detail(plant_id):
    plant_ref = db.reference('jenis_tanaman').child(plant_id)
    plant_data = plant_ref.get()
    
    data_requested = request.args.get('data_requested')  
    if plant_data:
        if data_requested in ['deskripsi_tanaman', 'id_tanaman', 'nama', 'url_gambar', 'url_produk']:
            response_data = plant_data.get(data_requested)
            return jsonify({
                "status": {
                    "code": 200,
                    "message": f"Success get {data_requested} based on ID"
                },
                "data": response_data
            }), 200
        elif data_requested == 'detail':
            return jsonify({
                "status": {
                    "code": 200,
                    "message": f"Success get detail based on ID"
                },
                "data": plant_data  
            }), 200
        else:
            return jsonify({
                "status": {
                    "code": 400,
                    "message": "Invalid data_requested parameter"
                }
            }), 400
    else:
        return jsonify({
            "status": {
                "code": 404,
                "message": "Plant not found"
            }
        }), 404

# API SOIL TYPE
@app.route('/soil', methods=['GET'])
def get_all_soils():
    soils_ref = db.reference('tanah')
    all_soils_data = soils_ref.get()
    
    if all_soils_data:
        return jsonify({
            "status": {
                "code": 200,
                "message": "Success get all soils",
            },
            "data": all_soils_data
        }), 200
    else:
        return jsonify({
            "status": {
                "code": 404,
                "message": "No soils found"
            }
        }), 404

@app.route('/soil/<string:soil_name>', methods=['GET'])
def soil_detail(soil_name):
    soil_ref = db.reference('tanah').child(soil_name)
    soil_data = soil_ref.get()
    
    data_requested = request.args.get('data_requested') 
    if soil_data:
        if data_requested in ['deskripsi_tanah', 'jenis', 'nama_tanah', 'rekomendasi_bibit']:
            response_data = soil_data.get(data_requested)
            return jsonify({
                "status": {
                    "code": 200,
                    "message": f"Success get {data_requested} based on ID"
                },
                "data": response_data
            }), 200

        elif data_requested == 'detail':
            return jsonify({
                "status": {
                    "code": 200,
                    "message": f"Success get detail based on ID"
                },
                "data": soil_data  
            }), 200

        else:
            return jsonify({
                "status": {
                    "code": 400,
                    "message": "Invalid data_requested parameter"
                }
            }), 400
    else:
        return jsonify({
            "status": {
                "code": 404,
                "message": "Soil not found"
            }
        }), 404

# MAIN FUNCTION
@app.route('/upload', methods=['POST'])
def upload_file():
    if 'file' not in request.files:
        return jsonify({'error': 'No file part'})

    file = request.files['file']

    if file.filename == '':
        return jsonify({'error': 'No selected file'})

    if file and allowed_file(file.filename):
        file_path = os.path.join('uploads', file.filename)
        file.save(file_path)

        class_name, confidence_score = predict_image(file_path)

        # Simpan hasil prediksi ke Firebase Realtime Database
        firebase_ref.push({
            'file_name': file.filename,
            'class_name': class_name,
            'confidence_score': float(confidence_score)
        })

        return jsonify({'success': 'File uploaded and predictions saved to Firebase'})
    else:
        return jsonify({'error': 'Failed to upload file or file type not allowed'})

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))


# @app.route("/prediction", methods=["POST"])
# def prediction_route():
#     if request.method == "POST":
#         image = request.files["image"]
#         if image and allowed_file(image.filename):
#             filename = secure_filename(image.filename)
#             image.save(os.path.join(app.config["UPLOAD_FOLDER"], filename))
#             image_path = os.path.join(app.config["UPLOAD_FOLDER"], filename)

#             class_name, confidence_score = predict_soil_type(image_path)

#             return jsonify({
#                 "status": {
#                     "code": 200,
#                     "message": "Success predicting"
#                 },
#                 "data": {
#                     "soil_types_prediction": class_name,
#                     "confidence": float(confidence_score),
#                 }
#             }), 200
#         else:
#             return jsonify({
#                 "status": {
#                     "code": 400,
#                     "message": "Invalid file format. Please upload a JPG, JPEG, or PNG image."
#                 }
#             }), 400
#     else:
#         return jsonify({
#             "status": {
#                 "code": 405,
#                 "message": "Method not allowed"
#             }
#         }), 405


# function ML models
# def get_soil_details(soil_name):
#     # Menerima query parameter yang menentukan tanah yang diinginkan
#     requested_soil = request.args.get('soil_name')

#     if requested_soil:
#         # Mengambil detail tanah berdasarkan query parameter
#         ref = db.reference('tanah')
#         soil_details_ref = ref.child(requested_soil)
#         soil_details = soil_details_ref.get()

#         if soil_details:
#             return {
#                 "deskripsi_tanah": soil_details.get("deskripsi_tanah", ""),
#                 "jenis": soil_details.get("jenis", ""),
#                 "nama_tanah": soil_details.get("nama_tanah", ""),
#                 "rekomendasi_bibit": soil_details.get("rekomendasi_bibit", "")
#             }
#         else:
#             return {
#                 "error": "Detail tanah tidak ditemukan di Firebase."
#             }
#     else:
#         return {
#             "error": "Silakan berikan parameter 'soil_name' untuk mendapatkan detail tanah."
#         }


# @app.route("/prediction", methods=["GET", "POST"])
# def prediction_route():
#     if request.method == "POST":
#         image = request.files["image"]
#         soil_name = request.args.get('soil_name')  

#         if image and allowed_file(image.filename):
#             filename = secure_filename(image.filename)
#             image.save(os.path.join(app.config["UPLOAD_FOLDER"], filename))
#             image_path = os.path.join(app.config["UPLOAD_FOLDER"], filename)

#             class_name, confidence_score = predict_soil_type(image_path)
#             soil_details = get_soil_details(soil_name)

#             if "error" in soil_details:
#                 return jsonify({
#                     "status": {
#                         "code": 404,
#                         "message": soil_details["error"]
#                     },
#                     "data": None
#                 }), 404
#             else:
#                 return jsonify({
#                     "status": {
#                         "code": 200,
#                         "message": "Success predicting"
#                     },
#                     "data": {
#                         "soil_types_prediction": class_name,
#                         "confidence": float(confidence_score),
#                         "soil_details": soil_details  
                        
#                     }
#                 }), 200

#     elif request.method == "GET":
#         soil_name = request.args.get('soil_name')
#         data_requested = request.args.get('data_requested')

#         if soil_name:
#             soil_details = get_soil_details(soil_name)

#             if "error" in soil_details:
#                 return jsonify({
#                     "status": {
#                         "code": 404,
#                         "message": soil_details["error"]
#                     },
#                     "data": None
#                 p.route('/soil/<string:soil_name>/recomendations-and-type', methods=['GET'])
# def get_soil_type_and_recomendations(soil_name):
#     soil_ref = db.reference(f'tanah/{soil_name}')  
#     soil_data = soil_ref.get()

#     if soil_data:
#         jenis_tanaman = []
#         rekomendasi_bibit = soil_data.get('rekomendasi_bibit', {})

#         for tanaman_id, status in rekomendasi_bibit.items():
#             if status:
#                 tanaman_ref = db.reference(f'jenis_tanaman/{tanaman_id}') 
#                 tanaman_data = tanaman_ref.get()
                
#                 if tanaman_data:
#                     jenis_tanaman.append({
#                         'deskripsi_tanaman': tanaman_data.get('deskripsi_tanaman', ''),
#                         'id_tanaman': tanaman_id,
#                         'nama': tanaman_data.get('nama', ''),
#                         'url_gambar': tanaman_data.get('url_gambar', ''),
#                         'url_produk': tanaman_data.get('url_produk', ''),
#                     }), 

#         return jsonify({'jenis_dan_rekomendasi': jenis_tanaman}), 200
#     else:
#         return jsonify({'error': 'Tanah tidak ditemukan'}), 404



# Fungsi untuk mendapatkan rekomendasi produk untuk dashboard
# @app.route('/dashboard-recommendations', methods=['GET'])
# def dashboard_recommendations():
#     try:
#         plants_ref = db.reference('jenis_tanaman')  # Mendapatkan referensi ke node jenis_tanaman
#         all_plants = plants_ref.get()  # Mendapatkan seluruh data tanaman

#         if all_plants:
#             all_plant_keys = list(all_plants.keys())
#             random_recommendations = random.sample(all_plant_keys, 10)
#             recommendations = {key: all_plants[key] for key in random_recommendations}

#             return jsonify({
#                     "status": {
#                         "code": 200,
#                         "message": "Success get recommendations"
#                     },
                    
#                     "data": recommendations

#                 }), 200
#         else:
#             return jsonify({"error": "Data tanaman tidak ditemukan."}), 404

#     except Exception as e:
#         return jsonify({'error': str(e)}), 500

