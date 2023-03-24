console.log(orderList);



var contractContainer= document.getElementById("contract-container");

var isContractOpen = false;
var orderId=2;

function getContract(button)
{
    // debugger;

        if(!isContractOpen)
        {
            isContractOpen = true;
            contractContainer.style.visibility="visible";
            button.disabled=true;
            appendImage(button);
        }






}
function appendImage(button) {
    const order= orderList.find((order)=>order.id==button.getAttribute("data-orderId"));
    const myImage = order.contract;
    const base64Image = "data:image/png;base64," + myImage;
    const imgElement = document.createElement("img");

    console.log(order);
    imgElement.src = base64Image;
    imgElement.style.width = "420px"; // set the width using CSS
    imgElement.style.height = "600px"; // set the height using CSS

    contractContainer.appendChild(imgElement);

    const divElement = document.createElement("div");
    divElement.innerHTML = `
    
      <button class="btn color2" onclick="closeContract()">X</button>
    `;
    contractContainer.appendChild(divElement);
}

function closeContract() {
    isContractOpen = false;
    var buttonList = document.querySelectorAll("[data-orderId]");

    buttonList.forEach((button)=>{button.removeAttribute("disabled")});
    console.log(buttonList);

    contractContainer.innerHTML = "";
    contractContainer.style.visibility="hidden";
}


