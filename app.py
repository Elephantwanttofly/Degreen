from flask import Flask, jsonify
from routes import get_all_plants, get_plant_by_id, create_new_plant, get_update_plant, delete_plant

app = Flask(__name__)

@app.route("/")
def index():
    return jsonify({
        "status": {
            "code": 200,
            "message": "Welcome to DeGreen",
        },
        "data": None
    }), 200

# Import rute-rute dari file routes.py
@app.route('/plants', methods=['GET'])
def plants_route():
    return get_all_plants()

@app.route('/plants/<string:plant_id>', methods=['GET'])
def plant_detail_route(plant_id):
    return get_plant_by_id(plant_id)

@app.route('/plants', methods=['POST'])
def create_plant():
    return create_new_plant()

@app.route('/plants/<string:plant_id>', methods=['PUT'])
def update_plant(plant_id):
    return get_update_plant(plant_id)  

@app.route('/plants/<string:plant_id>', methods=['DELETE'])
def delete_plant_route(plant_id):
    return delete_plant(plant_id)

if __name__ == "__main__":
    app.run(debug=True)