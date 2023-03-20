


console.log(myOrder);

const orderDay = new Date(myOrder.orderDate);
const eventDay = new Date(myOrder.timeHappen);
//depositday
const depositDay = new Date(orderDay);
depositDay.setDate(orderDay.getDate() + 3);

//warning day
const warningDay = new Date(eventDay);
warningDay.setDate(eventDay.getDate() - 14);

//confirm day
const confirmDay = new Date(eventDay);
confirmDay.setDate(eventDay.getDate() - 7);




const today = new Date();
//milisec
const total = eventDay - orderDay;




const formattedToday = formatDate(today);
const formattedOrderDay = formatDate(orderDay);
const formattedDepositDay = formatDate(depositDay);
const formattedWarningDay = formatDate(warningDay);
const formattedConfirmDay = formatDate(confirmDay);
const formattedEventDay = formatDate(eventDay);

// // Get the timeline element and each event element
const timeline = document.querySelector('.timeline');
const orderDayEl = document.getElementById('orderDay');
const depositDayEl = document.getElementById('depositDay');
const warningDayEl = document.getElementById('warningDay');
const eventDayEl = document.getElementById('eventDay');
const todayEl = document.getElementById('today');
const todayInfo = document.getElementById('todayInfo');
const depositPeriod = document.getElementById('depositPeriod');
const updatePeriod = document.getElementById('updatePeriod');
const warningPeriod = document.getElementById('warningPeriod');
const confirmPeriod = document.getElementById('confirmPeriod');




// // Set the position of each event on the timeline
const orderDayElPc = getPercentage(orderDay, depositDay, total);
const depositDayElPc = getPercentage(depositDay, warningDay, total);
const warningDayElPc = getPercentage(warningDay, confirmDay, total);
const eventDayElPc = getPercentage(confirmDay, eventDay, total);
const todayElPc = getPercentage(orderDay, today, total);
// debugger;
console.log(orderDayElPc, depositDayElPc, warningDayElPc, eventDayElPc);
console.log(orderDayEl, depositDayEl, warningDayEl, eventDayEl)

orderDayEl.style.width = orderDayElPc + '%';
depositDayEl.style.width = depositDayElPc + '%';
warningDayEl.style.width = warningDayElPc + '%';
eventDayEl.style.width = eventDayElPc + '%';
if(todayElPc>0 && todayElPc<100) {
    todayEl.style.left = todayElPc + '%';
}
//get info hover
todayInfo.innerHTML = `Today:  ${formattedToday} `;
depositPeriod.innerHTML = `from : ${formattedOrderDay} <br> to :${formattedDepositDay}`;
updatePeriod.innerHTML = `from : ${formattedDepositDay}	<br> to :${formattedWarningDay}`;
warningPeriod.innerHTML = `from : ${formattedWarningDay} <br> to :${formattedConfirmDay}`;
confirmPeriod.innerHTML = `from : ${formattedConfirmDay} <br> to :${formattedEventDay}`;


function getPercentage(start, end, total) {
    // const order;
    const elapsed = end - start;
    const percentage = (elapsed / total) * 100;
    return percentage;
}

function formatDate(myDay) {
    let year = myDay.getFullYear();
    let month = myDay.getMonth() + 1; // add 1 because getMonth() returns 0-indexed values
    let day = myDay.getDate();
    const formattedMyDay = `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
    return formattedMyDay;
}
