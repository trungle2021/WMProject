dayjs.extend(window.dayjs_plugin_weekday);
// dayjs.extend(window.dayjs_plugin_weekOfYear);
const date = dayjs(); // Create a Day.js instance

const weekday = date.weekday();
// const weekOfYear=dayjs().weekofyear();
// console(weekOfYear);
document.getElementById("app").innerHTML = `
<div class="calendar-month">
  <section class="calendar-month-header">
    <div
      id="selected-month"
      class="calendar-month-header-selected-month"
    ></div>
    <section class="calendar-month-header-selectors">
      <span id="previous-month-selector">Previous</span>
      <span id="present-month-selector">Present</span>
      <span id="next-month-selector">Next</span>
    </section>
  </section>

  <ol
    id="days-of-week"
    class="day-of-week"
  /></ol>

  <ol
    id="calendar-days"
    class="days-grid"
  >
  </ol>
</div>
`;

const WEEKDAYS = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
const TODAY = dayjs().format("YYYY-MM-DD");

const INITIAL_YEAR = dayjs().format("YYYY");
const INITIAL_MONTH = dayjs().format("M");

let selectedMonth = dayjs(new Date(INITIAL_YEAR, INITIAL_MONTH - 1, 1));
let currentMonthDays;
let previousMonthDays;
let nextMonthDays;
var venueCard = document.getElementById("card-container");
const daysOfWeekElement = document.getElementById("days-of-week");

WEEKDAYS.forEach((weekday) => {
    const weekDayElement = document.createElement("li");
    daysOfWeekElement.appendChild(weekDayElement);
    weekDayElement.innerText = weekday;
});

createCalendar();
initMonthSelectors();

function createCalendar(year = INITIAL_YEAR, month = INITIAL_MONTH) {
    const calendarDaysElement = document.getElementById("calendar-days");

    document.getElementById("selected-month").innerText = dayjs(
        new Date(year, month - 1)
    ).format("MMMM YYYY");

    removeAllDayElements(calendarDaysElement);

    currentMonthDays = createDaysForCurrentMonth(
        year,
        month,
        dayjs(`${year}-${month}-01`).daysInMonth()
    );

    previousMonthDays = createDaysForPreviousMonth(year, month);

    nextMonthDays = createDaysForNextMonth(year, month);

    const days = [...previousMonthDays, ...currentMonthDays, ...nextMonthDays];

    days.forEach((day) => {
        appendDay(day, calendarDaysElement);
    });
}

function appendDay(day, calendarDaysElement) {

    const dayLink = document.createElement("a");
    const dayElement = document.createElement("li");

    const dayElementClassList = dayElement.classList;
    dayElementClassList.add("calendar-day");
    const dayOfMonthElement = document.createElement("span");
    dayOfMonthElement.innerText = day.dayOfMonth;
    dayLink.appendChild(dayElement);

    dayLink.href = "/customers/orders/getvenue";
//ajax

    dayLink.addEventListener("click", function (event) {
        //add color for choosenday
        document.querySelectorAll(".calendar-day").forEach(day=>{
            day.classList.remove("clicked");
        });
        this.childNodes[0].classList.add("clicked");


        event.preventDefault(); // prevent the default behavior of the a tag

        // make the AJAX request
        const xhr = new XMLHttpRequest();

        xhr.open("POST", "/customers/orders/getvenue");
        xhr.setRequestHeader("Content-Type", "application/json");
        // let response = null;
        xhr.onload = function () {
            if(xhr.status===200) {
                // handle the response from the server
                let response = JSON.parse(xhr.responseText);
                let venueResponse = JSON.parse(response.venues);
                let bookedsResponse = JSON.parse(response.bookeds);

                let card = "";
                venueResponse.forEach(function (el) {
                    //append venue

                    card += appendVenue(el);
                    venueCard.innerHTML = card;
                    // console.log(card);

                });
                if (bookedsResponse != null) {
                    bookedsResponse.forEach(function (el) {
                        const button = document.querySelector(`[data-book="${el.bookedTime}"][data-venue="${el.venueId}"]`);
                        if (button) {
                            button.setAttribute('disabled', true);
                            button.setAttribute("class", "btn-danger");
                        }
                        console.log(button);
                    });
                }
                //tao link

                console.log(bookedsResponse);

                getOrderLink(day);
            }
            else{
                swal("Can't get Venues!Login Try Again!","Make sure that your connection is currently active/operational", "error");

            console.log(xhr.responseText);
            }

        };

        xhr.send(day.date);


    });


//end event click

    dayElement.appendChild(dayOfMonthElement);
    calendarDaysElement.appendChild(dayLink);

    if (!day.isCurrentMonth) {
        dayElementClassList.add("calendar-day--not-current");
    }

    if (day.date === TODAY) {
        dayElementClassList.add("calendar-day--today");
    }
}

function appendVenue(venue) {
debugger;
    let venueimages= venue.venueImagesById;
    let myVenueImg;
    if(Array.isArray(venueimages) && venueimages.length!==0)
    {
        myVenueImg=venueimages[0].url;
    }
    else{
        myVenueImg="/video/weddinglogo.png";
    }

    let venueCard =
        `<div class="card">
                    <img src="data:image/png;base64,${myVenueImg}" alt="Placeholder Image">
                    <div class="info">
                        <h2>${venue.venueName}</h2>
                        <p> Capacity: 
                        <span>Min :${venue.minPeople}--</span>  <span>--Max :${venue.maxPeople}</span> People</p>
                        <p>Table: From <span>${venue.minPeople/10} </span> To <span> ${(venue.maxPeople/10)}</span>
                      </p>
                        <div class="button-group">
                            <button data-book="Afternoon" data-order="true" data-venue="${venue.id.toString()}" class="button-primary">Afternoon Party</button>
                            <button data-book="Evening" data-order="true" data-venue="${venue.id.toString()}" class="">Everning Party</button>                                 
                        </div>
                    </div>
          </div>
        `
    return venueCard;

}

function getOrderLink(day) {
    const buttons = document.querySelectorAll('button[data-order="true"]');

    // console.log(buttons);
// Loop through the buttons and attach an event listener to each one
    buttons.forEach(button => {
        button.addEventListener('click', function (event) {
            // event.preventDefault(); // Prevent the default behavior of the button
            swal({
                title: "Are you sure to booking this Venue?",
                text: "Press OK to confirm this booking!",
                icon: "warning",
                buttons: true,
                dangerMode: true,
            })
                .then((booking) => {
                    if (booking) {
                         // Get the values of the data attributes
                            const bookType = this.getAttribute('data-book');
                            const venueId = this.getAttribute('data-venue');
                            // alert(bookType + venueId + day.date);
                            // Create a new XMLHttpRequest object
                            const xhr = new XMLHttpRequest();

                            // Define the AJAX request

                            xhr.open('POST', '/customers/orders/create');
                            xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');

                            // Define what should happen when the response is received
                            xhr.onload = function () {
                                // console.log(xhr.response);
                                if (xhr.status === 200) {
                                    console.log(xhr.response);
                                    // Handle successful response
                                    let myOrder = JSON.parse(xhr.responseText);
                                    // let orderId=myOrder.id
                                    // window.location.href="order/create-detail/"+orderId;
                                    // debugger;
                                    //chuyen sang post
                                    const form = document.createElement("form");
                                    form.method = "POST";

                                    form.action = "/customers/orders/create-detail";

                                    const input = document.createElement("input");
                                    input.type = "hidden";
                                    input.name = "orderId";
                                    input.value = myOrder.id;

                                    form.appendChild(input);
                                    document.body.appendChild(form);

                                    form.submit();

                                } else {
                                    swal("Error!", xhr.responseText, "error");


                                    console.log(xhr.responseText);
                                    // console.log(xhr.response);
                                    // Handle error
                                }
                            };

                            // Send the AJAX request with the data in JSON format
                            xhr.send(JSON.stringify({bookType: bookType, venueId: venueId, day: day.date}));

                    } else {
                        swal("Thank you. Please see other options!");
                    }
                });

            // if(confirm("Are You sure to book this venue for your party?"))

        });
    });
}


function removeAllDayElements(calendarDaysElement) {
    let first = calendarDaysElement.firstElementChild;

    while (first) {
        first.remove();
        first = calendarDaysElement.firstElementChild;
    }
}

function getNumberOfDaysInMonth(year, month) {
    return dayjs(`${year}-${month}-01`).daysInMonth();
}

function createDaysForCurrentMonth(year, month) {
    return [...Array(getNumberOfDaysInMonth(year, month))].map((day, index) => {
        return {
            date: dayjs(`${year}-${month}-${index + 1}`).format("YYYY-MM-DD"),
            dayOfMonth: index + 1,
            isCurrentMonth: true
        };
    });
}

function createDaysForPreviousMonth(year, month) {
    const firstDayOfTheMonthWeekday = getWeekday(currentMonthDays[0].date);

    const previousMonth = dayjs(`${year}-${month}-01`).subtract(1, "month");

    // Cover first day of the month being sunday (firstDayOfTheMonthWeekday === 0)
    const visibleNumberOfDaysFromPreviousMonth = firstDayOfTheMonthWeekday
        ? firstDayOfTheMonthWeekday - 1
        : 6;

    const previousMonthLastMondayDayOfMonth = dayjs(currentMonthDays[0].date)
        .subtract(visibleNumberOfDaysFromPreviousMonth, "day")
        .date();

    return [...Array(visibleNumberOfDaysFromPreviousMonth)].map((day, index) => {
        return {
            date: dayjs(
                `${previousMonth.year()}-${previousMonth.month() + 1}-${
                    previousMonthLastMondayDayOfMonth + index
                }`
            ).format("YYYY-MM-DD"),
            dayOfMonth: previousMonthLastMondayDayOfMonth + index,
            isCurrentMonth: false
        };
    });
}

function createDaysForNextMonth(year, month) {
    const lastDayOfTheMonthWeekday = getWeekday(
        `${year}-${month}-${currentMonthDays.length}`
    );

    const nextMonth = dayjs(`${year}-${month}-01`).add(1, "month");

    const visibleNumberOfDaysFromNextMonth = lastDayOfTheMonthWeekday
        ? 7 - lastDayOfTheMonthWeekday
        : lastDayOfTheMonthWeekday;

    return [...Array(visibleNumberOfDaysFromNextMonth)].map((day, index) => {
        return {
            date: dayjs(
                `${nextMonth.year()}-${nextMonth.month() + 1}-${index + 1}`
            ).format("YYYY-MM-DD"),
            dayOfMonth: index + 1,
            isCurrentMonth: false
        };
    });
}

function getWeekday(date) {
    return dayjs(date).weekday();
}

function initMonthSelectors() {
    document
        .getElementById("previous-month-selector")
        .addEventListener("click", function () {
            selectedMonth = dayjs(selectedMonth).subtract(1, "month");
            createCalendar(selectedMonth.format("YYYY"), selectedMonth.format("M"));
        });

    document
        .getElementById("present-month-selector")
        .addEventListener("click", function () {
            selectedMonth = dayjs(new Date(INITIAL_YEAR, INITIAL_MONTH - 1, 1));
            createCalendar(selectedMonth.format("YYYY"), selectedMonth.format("M"));
        });

    document
        .getElementById("next-month-selector")
        .addEventListener("click", function () {
            selectedMonth = dayjs(selectedMonth).add(1, "month");
            createCalendar(selectedMonth.format("YYYY"), selectedMonth.format("M"));
        });
}
