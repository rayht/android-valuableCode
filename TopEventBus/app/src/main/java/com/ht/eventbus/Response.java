package com.ht.eventbus;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author Leiht
 * Date 2018-05-25
 */
public class Response implements Parcelable {
    // 响应的对象对应的JSON字符串
    private String data;

    public String getData() {
        return data;
    }

    //反序列化
    protected Response(Parcel in) {
        data = in.readString();
    }

    public Response(String data) {
        this.data = data;
    }

    public static final Creator<Response> CREATOR = new Creator<Response>() {
        @Override
        public Response createFromParcel(Parcel in) {
            return new Response(in);
        }

        @Override
        public Response[] newArray(int size) {
            return new Response[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    //序列化
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(data);
    }
}
