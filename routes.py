from flask import jsonify, request

plants_data = [
    {
        "id": "201N",
        "name": "Nanas",
        "description": "Nanas adalah ...........",
        "image_url": "http://www.Strawwberry.com",
        "marketplace_link": "http://www.tokopedia.com/bibit_nanas"
    },
    {
        "id": "202S",
        "name": "Strawberry",
        "description": "Strawberry adalah ...........",
        "image_url": "http://www.strawberry.com",
        "marketplace_link": "http://www.tokopedia.com/bibit_strawberry"
    }
    # data lainnya
]

#API TANAMAN

#melihat semua list jenis tanaman
def get_all_plants():
    return jsonify(plants_data)

#melihat jenis tanaman berdasarkan ID
def get_plant_by_id(plant_id):
    plant = next((plant for plant in plants_data if plant["id"] == plant_id), None)
    if plant:
        return jsonify(plant)
    else:
        return jsonify({
            "status":
                {
                    "code": 404,
                    "message": "Plant not found",
                },
            "data": None
    }), 404

#membuat data baru untuk tanaman [post]
from flask import jsonify, request, abort

# ...

def create_new_plant():
    if not request.json or not 'id' in request.json:
        abort(400)  # Jika permintaan tidak dalam format JSON atau tidak memiliki data 'id', kirim kode status 400 Bad Request
    else:    
        global plants_data
        request_data = request.json  # Menggunakan .json langsung dari permintaan
        new_plant = {
            "id": request_data["id"],
            "name": request_data.get("name", ""),
            "description": request_data.get("description", ""),
            "image_url": request_data.get("image_url", ""),
            "marketplace_link": request_data.get("marketplace_link", "")
        }
        plants_data.append(new_plant)
        response = {
            "status": {
                "code": 201,
                "message": "Plant created successfully"
            },
            "data": new_plant
        }
        return jsonify(response), 201

#mengupdate data tanaman
def get_update_plant(plant_id):
    for plant in plants_data:
        if plant["id"] == plant_id:
            updated_data = request.json  
            plant.update(updated_data)  
            return jsonify({
                "status": {
                    "code": 200,
                    "message": "Plant information updated successfully",
                },
                "data": plant  
            }), 200
    return jsonify({
        "status": {
            "code": 404,
            "message": "Failed to Update Plant",
        },
        "data": None
    }), 404


#menghapus tanaman
def delete_plant(plant_id):
    global plants_data
    initial_length = len(plants_data)
    plants_data = [plant for plant in plants_data if plant["id"] != plant_id]
    if len(plants_data) < initial_length:
        return jsonify({
            "status": {
                "code": 200,
                "message": "Plant deleted successfully",
            },
            "data": None
        }), 200
    else:
        return jsonify({
            "status": {
                "code": 404,
                "message": "Plant not found",
            },
            "data": None
        }), 404

#FINISHED API TANAMAN

#API SOIL PREDICTION