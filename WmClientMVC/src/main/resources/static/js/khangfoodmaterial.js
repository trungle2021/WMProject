
var foodId= document.getElementById("foodId");
var materialId=document.getElementById("materialId");
var materialCount=document.getElementById("material-count");
var addBtn= document.getElementById("add-material");

addBtn.addEventListener('click',callAJAXnewMaterial);

    document.addEventListener('DOMContentLoaded', function() {
console.log(materialList);

    materialList.forEach(function(material) {
    var optionText = material.materialName;
    var optionValue = material.id;
    var option = document.createElement('option');
    option.value = optionValue;
    option.text = optionText;
    // alert("a");
    // alert(material.materialName);
    // alert(option);
        materialId.appendChild(option);
});
        materialId.addEventListener('change', function() {
    var selectedMaterialId = this.value;
    if (selectedMaterialId) {
    var selectedMaterial = materialList.find((obj)=>obj.id==selectedMaterialId);
    if(selectedMaterial.unit==null){
        document.getElementById('unit-display').textContent = 'N/A';
    }else{
        document.getElementById('unit-display').textContent = selectedMaterial.unit;
    }
} else {
    document.getElementById('unit-display').textContent = 'N/A';
}
});
});



function callAJAXnewMaterial(){

    if(materialCount.value === "" || isNaN(materialCount.value) || materialCount.value > 1000 || materialCount.value < 0)
    {
        console.log(materialCount,materialCount.value);
        swal("Warning!", "Please enter a valid material count between 0 and 1000!", "warning");

    }
else{

    const xhr = new XMLHttpRequest();

    xhr.open("POST", "/staff/food/addMaterial");
    xhr.setRequestHeader("Content-Type", "application/json");
    let response=null;

    //
        let foodIdVal=foodId.value;
        let materialIdVal=materialId.value;
        let count=materialCount.value;
    // console.log(requestSvList);
    console.log(materialIdVal);
    let data = JSON.stringify({foodId: foodIdVal,materialId: materialIdVal,materialCount: count});
    console.log(data);
    //
    xhr.onload = function() {
        // handle the response from the server
        if (xhr.status === 200) {
            // get foodId
            response = JSON.parse(xhr.responseText);
            swal("Success!", response.message, "success");

        }
        else{

            swal("Fail!", "Oops!Some thing Wrong!Make sure you fill correct and not duplicate!", "error");

            // Handle error
        }//tao link
    };
    xhr.send(data);

}
}