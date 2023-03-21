
// console.log(foodList,serviceList,myOrder);

const newService = document.getElementById("btn-newService");
const newMain = document.getElementById("btn-newMain");
var serviceContaier = document.getElementById("service");
newService.addEventListener("click", addService);
var mainContainer = document.getElementById("main");
newMain.addEventListener("click", addMain);
var foodTable = document.getElementById("food-table");
var serviceTable = document.getElementById("service-table");
var total=document.getElementById("total");
var foodOption="";
var serviceOption="";
const confirm=document.getElementById("confirm");
confirm.addEventListener("click",callAJAXnewOrder);
var tableAmount=document.getElementById("tableAmount");
tableAmount.addEventListener("change",changeTableAmount);
var tbCount=0;
foodList.forEach((food)=> {
    if(food.foodType ==="main") {
        foodOption += `<option value="${food.id}"> ${food.foodName}
                </option>`
    }
});
serviceList.forEach((service)=>{
    serviceOption+=`<option value="${service.id}"> ${service.serviceName}
                </option>`

})
        setDefault();
function addMain() {
    let list = document.querySelectorAll("[data-main]");
    //add main

    if (list.length < 5) {
        const foodSelector = `
        <label data-label="main" >Main dish  ${list.length + 1} :</label>
        <select data-index="food" class="form-control select-box" onchange="getId(this)">
        <option value="default"> Choose Dish </option>
         ${foodOption}  

        </select>
        <button onclick="deleteMain(this)" class="btn btn-danger"><i class="fas fa-trash-alt"></i> Remove</button>
      `



        let newContainer = document.createElement("div");
        newContainer.setAttribute("data-main", `${list.length}`);
        newContainer.setAttribute("class", "new-container");
        // alert(newContainer);

        newContainer.innerHTML = foodSelector;

        mainContainer.appendChild(newContainer);

        // getOrderList();

    } else {

        swal("Warning!", "Maximum Dish Main is 5!", "warning");
    }
}


function deleteMain(button) {
    button.parentNode.remove();
    let list = document.querySelectorAll("[data-main]");

    let listLabel = document.querySelectorAll(`[data-label="main"]`);
    let i = 1;
    listLabel.forEach(label => {

        label.innerHTML = `Main Dish ${i} :`
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
    <label data-label="service">Service  ${list.length + 1} :</label>
    <select data-index="service" class="form-control select-box" onchange="getId(this)" >
    <option value="default"> Choose Service </option>
        ${serviceOption}          
    </select>
    <button onclick="deleteService(this)" class="btn btn-danger"><i class="fas fa-trash-alt"> </i> Remove</button>
  `

        // <option th:each="service : ${serviceList}" th:value="${service.getId()}" th:text="${service.getServiceName()}"></option>

        var newContainer = document.createElement("div");
        newContainer.setAttribute("data-service", `${list.length}`);
        newContainer.setAttribute("class", "new-container");


        newContainer.innerHTML = serviceSelector;

        serviceContaier.appendChild(newContainer);
        // getOrderList();

    } else {

        swal("Warning!", "Max service is 4!", "warning");
    }

}
function deleteService(button) {
    button.parentNode.remove();
    let list = document.querySelectorAll("[data-service]");

    let listLabel = document.querySelectorAll(`[data-label="service"]`);
    let i = 1;
    listLabel.forEach(label => {

        label.innerHTML = `Service ${i} :`
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
        <th>Dishes</th>
        <th>Price</th>
      </tr>
    `;
}

function appendFood(obj) {
    const formattedNumber = obj.price.toLocaleString("en-US", {style: "currency", currency: "USD"})
    const tdFood = `
 
        <td data-foodId="${obj.id}">${obj.foodName}</td>
        <td>${formattedNumber} </td>
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
        <th>Service</th>
        <th>Price</th>
    </tr>
    <tr>
        <td>Basic Party Services</td> 
        <td>Free</td>
    </tr>
    `;
}

function appendService(obj) {
    const formattedNumber = obj.price.toLocaleString("en-US", {style: "currency", currency: "USD"})
    const tdService = `
   
        <td data-serviceId ="${obj.id}">
            ${obj.serviceName}</td>
        <td>${formattedNumber} </td>
  
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
            swal("Warning!", "Duplicate selection, please check again!", "warning");

            // console.log(element.getAttribute("data-index"));


        }
        else if (order.value !== "default" && order.getAttribute("data-index") === "food") {

            foodValues.push(order.value);

        }
        else if (order.value !== "default" && order.getAttribute("data-index") === "service") {
            serviceValues.push(order.value);


        }


    });

    element.value = temp;

    // console.log(foodValues);
    // console.log(foodList);

    //get foodlist
    let myNewFoodList=foodList.filter(food=>foodValues.includes(food.id.toString()));
    console.log(myNewFoodList);
    console.log(foodValues);
    let myNewServiceList=serviceList.filter(service=>serviceValues.includes(service.id.toString()));
    //filter foodList
    console.log(myNewFoodList);
    myNewFoodList.forEach((obj) => {

        appendFood(obj);
    });
    myNewServiceList.forEach((obj) => {
        appendService(obj);
    });
    getTotalPrice(myNewFoodList,myNewServiceList,myOrder);


    //call AJAX here

}

function getTotalPrice(orderFoodList,orderServiceList,myOrder)
{
    // alert("o4k");
    let foodTotalPrice=  orderFoodList.reduce((acc,food)=>acc+food.price,0);
    let serviceTotalPrice= orderServiceList.reduce((acc,service)=>acc+service.price,0);
    let tableNum=tableAmount.value;
    let totalPrice= foodTotalPrice*tableNum + serviceTotalPrice+myOrder.venues.price;
    // console.log(total);

    const formattedNumber = totalPrice.toLocaleString("en-US", {style: "currency", currency: "USD"})
    console.log(formattedNumber);
    total.innerHTML=formattedNumber;


    ///call after load html



}
//viet lây du liệu ra thymleaf ss với list foodValues và serviceValues lấy giá trị show hình hover và giá.
// thymleaf show du liệu combobox.
function callAJAXnewOrder(){
    if(tableAmount.value===0)
    {
        swal("Warning!", "Please add the table number to get a more accurate price!", "warning");

    }

    const xhr = new XMLHttpRequest();

    xhr.open("POST", "/customers/orders/myorder/order-detail/customer-update");
    xhr.setRequestHeader("Content-Type", "application/json");
    let response=null;

    //
    let foodIdList = [];
    let requestFoodList=document.querySelectorAll("[data-foodId]")
    requestFoodList.forEach((el) => {
        let id = el.getAttribute("data-foodId");
        foodIdList.push(id);
    });
    //get serviceList
    let svIdList = [];
    let requestSvList=document.querySelectorAll("[data-serviceId]")
    requestSvList.forEach((el) => {
        let id = el.getAttribute("data-serviceId");
        svIdList.push(id);
    });
    // console.log(requestSvList);
    // console.log(requestFoodList);
    let data = JSON.stringify({orderId: myOrder.id, foodList: foodIdList, serviceList: svIdList,table:tbCount});
    console.log(data);
    //
    xhr.onload = function() {
        // handle the response from the server
        if (xhr.status === 200) {
            // get foodId


            response = JSON.parse(xhr.responseText);
            swal("Success!", response.message, "success");

            setTimeout(function() {
                window.location.href = "/";
            }, 2000);

        }
        else{
            console.error(xhr.statusText);
            // alert("Oops!Some thing Wrong!");
            swal("Fail!", "Oops!Some thing Wrong!", "error");
            // Handle error
        }
        //tao link
    };

    if(foodIdList.length>=6){
        xhr.send(data);}
    else{

        swal("Warning!", "The minimum number of main dishes is 4!", "warning");
    }

}

function reloadTotal()
{
    let myOrderList = document.querySelectorAll("[data-index]");
    let foodValues = [];
    let serviceValues = [];
    // let serviceList = document.querySelectorAll(`[data-index="service"]`);
    clearFood();
    clearService();
    myOrderList.forEach((order) => {
        if (order.value !== "default" && order.getAttribute("data-index") === "food") {

            foodValues.push(order.value);

        }
        else if (order.value !== "default" && order.getAttribute("data-index") === "service") {
            serviceValues.push(order.value);


        }


    });



    // console.log(foodValues);
    // console.log(foodList);

    //get foodlist
    let myNewfl=foodList.filter(food=>foodValues.includes(food.id.toString()));
    let myNewsl=serviceList.filter(service=>serviceValues.includes(service.id.toString()));
    //filter foodList
    console.log(myNewfl);
    myNewfl.forEach((obj) => {

        appendFood(obj);
    });
    myNewsl.forEach((obj) => {
        appendService(obj);
    });
    getTotalPrice(myNewfl,myNewsl,myOrder);
    tbCount=tableAmount.value;

    //call AJAX here
}

function changeTableAmount()
{
    const value=this.value;
    const minTable= Math.ceil(myOrder.venues.minPeople/10);
    const maxTable=Math.ceil(myOrder.venues.maxPeople/10);
    if( value<minTable)
    {
        swal("Warning!", "The minimum required number of tables has not been reached yet!", "warning");

        this.value=0;
        reloadTotal();
    }
    else if(value>maxTable)
    {
        swal("Warning!", "The maximum required number of tables has not been reached yet!", "warning");


        this.value=0;
        reloadTotal();}
    else{
        reloadTotal();
    }


}


function setDefault()
{
    debugger;
myOrder.foodDetailsById.forEach((foodDetail)=>{
    if(foodDetail.foodByFoodId.foodType==="main")
    {
        addMainupdate(foodDetail.foodByFoodId.id);
    }
    else if(foodDetail.foodByFoodId.foodType==="starter")
    {
       let starter= document.querySelector('select[data-index="food"][data-starter=""]');
        starter.value= foodDetail.foodByFoodId.id;
        getId(starter);
    }
    else if(foodDetail.foodByFoodId.foodType==="dessert")
    {
        let dessert= document.querySelector('select[data-index="food"][data-dessert=""]');
        dessert.value= foodDetail.foodByFoodId.id;
        getId(dessert);
    }


});
    myOrder.serviceDetailsById.forEach((sDetail)=>{
            addServiceUpdate(sDetail.servicesByServiceId.id);
    });
    tbCount=tableAmount.value;
}

function addMainupdate(foodId) {
    let list = document.querySelectorAll("[data-main]");
    //add main


        const foodSelector = `
        <label data-label="main" >Main dish  ${list.length + 1} :</label>
        <select data-index="food" class="form-control select-box" onchange="getId(this)">
        <option value="default"> Choose Dish </option>
         ${foodOption}  

        </select>
        <button onclick="deleteMain(this)" class="btn btn-danger"><i class="fas fa-trash-alt"></i> Remove</button>
      `



        let newContainer = document.createElement("div");
        newContainer.setAttribute("data-main", `${list.length}`);
        newContainer.setAttribute("class", "new-container");
        // alert(newContainer);

        newContainer.innerHTML = foodSelector;
        debugger;

        mainContainer.appendChild(newContainer);
             let selectElement = newContainer.querySelector('select[data-index="food"]');
             selectElement.value = foodId;
             getId(selectElement);
        // getOrderList();


}


function addServiceUpdate(serviceId) {

    let list = document.querySelectorAll("[data-service]");

        const serviceSelector = `
    <label data-label="service">Service  ${list.length + 1} :</label>
    <select data-index="service" class="form-control select-box" onchange="getId(this)" >
    <option value="default"> Choose Service </option>
        ${serviceOption}          
    </select>
    <button onclick="deleteService(this)" class="btn btn-danger"><i class="fas fa-trash-alt"> </i> Remove</button>
  `

        // <option th:each="service : ${serviceList}" th:value="${service.getId()}" th:text="${service.getServiceName()}"></option>

        var newContainer = document.createElement("div");
        newContainer.setAttribute("data-service", `${list.length}`);
        newContainer.setAttribute("class", "new-container");


        newContainer.innerHTML = serviceSelector;

        serviceContaier.appendChild(newContainer);
        // getOrderList();
    let selectElement = newContainer.querySelector('select[data-index="service"]');
    selectElement.value = serviceId;
    getId(selectElement);


}