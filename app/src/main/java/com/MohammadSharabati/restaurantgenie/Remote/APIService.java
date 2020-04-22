package com.MohammadSharabati.restaurantgenie.Remote;


import com.MohammadSharabati.restaurantgenie.Model.DataMessage;
import com.MohammadSharabati.restaurantgenie.Model.MyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
/**
 * Created by Mohammad Sharabati.
 */
public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAAmt6s1s:APA91bHP15jPLHPwG7CHZASciQ4x5Qm1CAZXXMYAN5tgzvu8-oK-jx1eNqL2DW-9Tri9Uskovjpxt142VT18OIybUuNt0ETuO3rx1Og5QqNnzqG2NbteakJ0KPpX7rk2AdU2CoJu_x0X"
            }

    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body DataMessage body);
}
