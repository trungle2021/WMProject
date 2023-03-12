// console.log(foodList,serviceList,myOrder);

const newService = document.getElementById("btn-newService");
const newMain = document.getElementById("btn-newMain");
var serviceContaier = document.getElementById("service");
newService.addEventListener("click", addService);
var mainContainer = document.getElementById("main");
newMain.addEventListener("click", addMain);
var foodTable = document.getElementById("food-table");
var serviceTable = document.getElementById("service-table");
var total = document.getElementById("total");
var foodOption = "";
var serviceOption = "";
const confirm = document.getElementById("confirm");
confirm.addEventListener("click", callAJAXnewOrder);

foodList.forEach((food) => {
    if (food.foodType === "main") {
        foodOption += `<option value="${food.id}"> ${food.foodName}
                </option>`
    }
});
serviceList.forEach((service) => {
    serviceOption += `<option value="${service.id}"> ${service.serviceName}
                </option>`

})

function addMain() {
    let list = document.querySelectorAll("[data-main]");
    //add main

    if (list.length < 5) {
        const foodSelector = `
        <label data-label="main" >Món Chính  ${list.length + 1} :</label>
        <select data-index="food" class="form-control select-box" onchange="getId(this)">
        <option value="default"> Chọn Món Ăn </option>
         ${foodOption}  

        </select>
        <button onclick="deleteMain(this)" class="btn btn-danger"><i class="fas fa-trash-alt"></i></button>
      `


        let newContainer = document.createElement("div");
        newContainer.setAttribute("data-main", `${list.length}`);
        newContainer.setAttribute("class", "new-container");
        // alert(newContainer);

        newContainer.innerHTML = foodSelector;

        mainContainer.appendChild(newContainer);

        // getOrderList();

    } else {
        alert("max Main is 5!");
    }
}


function deleteMain(button) {
    button.parentNode.remove();
    let list = document.querySelectorAll("[data-main]");

    let listLabel = document.querySelectorAll(`[data-label="main"]`);
    let i = 1;
    listLabel.forEach(label => {

        label.innerHTML = `Món Chính ${i} :`
        i++;
    })


    //nhớ remove ben order info use onload

    // deleteContainer.parentNode.removeChild(deleteContainer);
    reloadTotal();
    // confirm.addEventListener("click",callAJAXnewOrder);

}

function addService() {

    let list = document.querySelectorAll("[data-service]");
    if (list.length < 4) {
        const serviceSelector = `
    <label data-label="service">Dịch Vụ ${list.length + 1} :</label>
    <select data-index="service" class="form-control select-box" onchange="getId(this)" >
    <option value="default"> Chọn Dịch Vụ </option>
        ${serviceOption}          
    </select>
    <button onclick="deleteService(this)" class="btn btn-danger"><i class="fas fa-trash-alt"></i></button>
  `

        // <option th:each="service : ${serviceList}" th:value="${service.getId()}" th:text="${service.getServiceName()}"></option>

        var newContainer = document.createElement("div");
        newContainer.setAttribute("data-service", `${list.length}`);
        newContainer.setAttribute("class", "new-container");


        newContainer.innerHTML = serviceSelector;

        serviceContaier.appendChild(newContainer);
        // getOrderList();

    } else {
        alert("max service is 4!");
    }

}

function deleteService(button) {
    button.parentNode.remove();
    let list = document.querySelectorAll("[data-service]");

    let listLabel = document.querySelectorAll(`[data-label="service"]`);
    let i = 1;
    listLabel.forEach(label => {

        label.innerHTML = `Dịch Vụ ${i} :`
        i++;

    })


    //nhớ remove ben order info use onload

    // deleteContainer.parentNode.removeChild(deleteContainer);
    reloadTotal();
    // confirm.addEventListener("click",callAJAXnewOrder);

    // getOrderList();
}

function clearFood() {
    // Remove all child elements of foodTable
    while (foodTable.firstChild) {
        foodTable.removeChild(foodTable.firstChild);
    }
    // Reset the innerHTML of foodTable with the table header
    foodTable.innerHTML = `
      <tr>
        <th>Món Ăn</th>
        <th>Giá</th>
      </tr>
    `;
}

function appendFood(obj) {

    const tdFood = `
 
        <td data-foodId="${obj.id}">${obj.foodName}</td>
        <td>${obj.price} VND</td>
  `
    let trFood = document.createElement("tr");
    trFood.innerHTML = tdFood;
    foodTable.appendChild(trFood);

}

function clearService() {
    // Remove all child elements of servicetable
    while (serviceTable.firstChild) {
        serviceTable.removeChild(serviceTable.firstChild);
    }
    // Reset the innerHTML of svTable with the table header
    serviceTable.innerHTML = `
    <tr>
        <th>Dich Vụ</th>
        <th>Giá</th>
    </tr>
    <tr>
        <td>Dịch Vụ Tiệc Cơ Bản</td> 
        <td>Miễn Phí</td>
    </tr>
    `;
}

function appendService(obj) {

    const tdService = `
   
        <td data-serviceId ="${obj.id}">
            ${obj.serviceName}</td>
        <td>${obj.price} VND</td>
  
  `
    let trService = document.createElement("tr");
    trService.innerHTML = tdService;
    serviceTable.appendChild(trService);
    // console.log(obj);
    // alert("1");

}

function getId(element) {
    let myOrderList = document.querySelectorAll("[data-index]");
    let foodValues = [];
    let serviceValues = [];
    let temp = element.value;
    // let serviceList = document.querySelectorAll(`[data-index="service"]`);
    clearFood();
    clearService();
    myOrderList.forEach((order) => {
        if (order.getAttribute("data-index") === element.getAttribute("data-index") && order.value === element.value && order !== element) {
            temp = "default";
            alert("Lựa chọn trùng,xin kiểm tra lại!");
            // console.log(element.getAttribute("data-index"));


        } else if (order.value !== "default" && order.getAttribute("data-index") === "food") {

            foodValues.push(order.value);

        } else if (order.value !== "default" && order.getAttribute("data-index") === "service") {
            serviceValues.push(order.value);


        }


    });

    element.value = temp;

    // console.log(foodValues);
    // console.log(foodList);

    //get foodlist
    let myNewFoodList = foodList.filter(food => foodValues.includes(food.id.toString()));
    console.log(myNewFoodList);
    console.log(foodValues);
    let myNewServiceList = serviceList.filter(service => serviceValues.includes(service.id.toString()));
    //filter foodList
    console.log(myNewFoodList);
    myNewFoodList.forEach((obj) => {

        appendFood(obj);
    });
    myNewServiceList.forEach((obj) => {
        appendService(obj);
    });
    getTotalPrice(myNewFoodList, myNewServiceList, myOrder);


    //call AJAX here

}

function getTotalPrice(orderFoodList, orderServiceList, myOrder) {
    // alert("o4k");
    let foodTotalPrice = orderFoodList.reduce((acc, food) => acc + food.price, 0);
    let serviceTotalPrice = orderServiceList.reduce((acc, service) => acc + service.price, 0);
    let totalPrice = foodTotalPrice + serviceTotalPrice + myOrder.venues.price;
    console.log(total);
    total.innerHTML = totalPrice.toString() + " VND";


    ///call after load html


}

//viet lây du liệu ra thymleaf ss với list foodValues và serviceValues lấy giá trị show hình hover và giá.
// thymleaf show du liệu combobox.
function callAJAXnewOrder() {

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "/order/create-order");
    xhr.setRequestHeader("Content-Type", "application/json");
    let response = null;

    //
    let foodIdList = [];
    let requestFoodList = document.querySelectorAll("[data-foodId]")
    requestFoodList.forEach((el) => {
        let id = el.getAttribute("data-foodId");
        foodIdList.push(id);
    });
    //get serviceList
    let svIdList = [];
    let requestSvList = document.querySelectorAll("[data-serviceId]")
    requestSvList.forEach((el) => {
        let id = el.getAttribute("data-serviceId");
        svIdList.push(id);
    });
    console.log(requestSvList);
    console.log(requestFoodList);
    let data = JSON.stringify({orderId: myOrder.id, foodList: foodIdList, serviceList: svIdList});
    console.log(data);
    //
    xhr.onload = function () {
        // handle the response from the server
        if (xhr.status === 200) {
            // get foodId


            response = JSON.parse(xhr.responseText);
            alert(response.message);
            window.location.href = "/index";

        } else {
            console.error(xhr.statusText);
            alert("có gì đó sai sai!");
            // Handle error
        }
        //tao link
    };

    if (foodIdList.length >= 6) {
        xhr.send(data);
    } else {
        alert("Số Món Ăn Chính Tối Thiểu là 4!")
    }

}

function reloadTotal() {
    let myOrderList = document.querySelectorAll("[data-index]");
    let foodValues = [];
    let serviceValues = [];
    // let serviceList = document.querySelectorAll(`[data-index="service"]`);
    clearFood();
    clearService();
    myOrderList.forEach((order) => {
        if (order.value !== "default" && order.getAttribute("data-index") === "food") {

            foodValues.push(order.value);

        } else if (order.value !== "default" && order.getAttribute("data-index") === "service") {
            serviceValues.push(order.value);


        }


    });


    // console.log(foodValues);
    // console.log(foodList);

    //get foodlist
    let myNewfl = foodList.filter(food => foodValues.includes(food.id.toString()));
    let myNewsl = serviceList.filter(service => serviceValues.includes(service.id.toString()));
    //filter foodList
    console.log(myNewfl);
    myNewfl.forEach((obj) => {

        appendFood(obj);
    });
    myNewsl.forEach((obj) => {
        appendService(obj);
    });
    getTotalPrice(myNewfl, myNewsl, myOrder);


    //call AJAX here
}