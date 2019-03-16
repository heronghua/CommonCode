package cn.make1.vangelis.makeonec.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Ronghua.He in Make1
 * @date 2019/3/16
 * Company:Make1
 * Email:Ronghua.He@make1.cn
 */
public class ResponseEntity<T extends Parcelable> implements Parcelable, Serializable {

    private int code;
    private String description;
    private String requestId;
    private String timestamp;
    private T response;


    public ResponseEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.description);
        dest.writeString(this.requestId);
        dest.writeString(this.timestamp);
        dest.writeParcelable(this.response, flags);
    }

    protected ResponseEntity(Parcel in) {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        this.code = in.readInt();
        this.description = in.readString();
        this.requestId = in.readString();
        this.timestamp = in.readString();
        this.response = in.readParcelable(((Class) params[0]).getClassLoader());
    }

    public static final Creator<ResponseEntity> CREATOR = new Creator<ResponseEntity>() {
        @Override
        public ResponseEntity createFromParcel(Parcel source) {
            return new ResponseEntity(source);
        }

        @Override
        public ResponseEntity[] newArray(int size) {
            return new ResponseEntity[size];
        }
    };


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
