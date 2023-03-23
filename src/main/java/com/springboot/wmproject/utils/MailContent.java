package com.springboot.wmproject.utils;


public class MailContent {


    public static  String getContent(String customer,String orderId,String venue,String bookingDay,String eventDay,String status,String company) {
        String mailOrderContent =
                "Dear "+customer+",\n" +
                        "<br>" +
                        "I am writing in regards to the order you have placed with us " + orderId + " for your event. We are pleased to be working with you to make your event a success.\n" +
                        "<p>Venue: "+venue+"</p>\n" +
                        "<p>Booking Date: " + bookingDay + "</p>\n" +
                        "<p>Event Date: " + eventDay + "</p>\n" +
                        "<p>Booking Status: " + status + "</p>\n" +

                        "<p>As per our contract, we would like to remind you that a deposit is required to secure the order. It has been 3 days since the order was placed and we kindly request that you deposit the required amount as soon as possible to avoid any delays or inconveniences.</p>\n" +
                        "</p>" +
                        "<p>Please note that the deposit can be made by cash </p>" +
                        "\n" +
                        "<p>If you have any questions or concerns, please do not hesitate to contact us at 0703660454 or Email khangkhang@gmail.com. We look forward to hearing from you soon and continuing our partnership.</p>\n" +
                        "<br>" +
                        "Best regards,\n" +
                        "<br>" +
                        company;

        return mailOrderContent;
    }
        public  static String getRefundMail(String customer,String venue,String bookingDay,String eventDay,String refund) {


    String content="Dear,\n" +customer+
            "\n<br>" +
            "    Thank you for your email regarding your cancellation and refund request.We apologize for any inconvenience\n" +
            "    caused and we will do our best to accommodate your request.\n" +
            "<br>" +
                "<p>Venue: "+venue+"</p>\n" +
                "<p>Booking Date: " + bookingDay + "</p>\n" +
                "<p>Event Date: " + eventDay + "</p>\n" +
                "<p>Refund amount: $:" + refund + "</p>\n" +

            "    Regarding your request to meet and receive your refund by cash, we would like to inform you that we have a standard\n" +
            "    refund policy where refunds are issued through the original payment method used during the purchase.However, we\n" +
            "    understand that you may have a preference for a different refund method.\n" +
            "<br>" +
            "    Please let us know the reason why you prefer to receive your refund by cash and we will do our best to find\n" +
            "    a suitable solution for you.Alternatively, we can arrange a bank transfer to your account, which would be a safe\n" +
            "    and secure method to receive your refund.\n" +
            "<br>" +
            "            Please contact us at your earliest convenience to discuss the available options.\n" +
            "<br>" +
            "            Thank you for your understanding and we hope to hear back from you soon.\n" +
            "<br>" +
            "            Best regards,<br>" +
                     "KTK-Wedding";
            return content;
        }
}
