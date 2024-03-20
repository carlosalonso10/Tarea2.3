package com.example.tarea23;
// Photograph.java
import android.os.Parcel;
import android.os.Parcelable;

public class Photograph implements Parcelable {
    private int id;
    private byte[] image;
    private String description;

    public Photograph() {
    }

    // Constructor con argumentos
    public Photograph(byte[] image, String description) {
        this.image = image;
        this.description = description;
    }

    protected Photograph(Parcel in) {
        id = in.readInt();
        image = in.createByteArray();
        description = in.readString();
    }

    public static final Creator<Photograph> CREATOR = new Creator<Photograph>() {
        @Override
        public Photograph createFromParcel(Parcel in) {
            return new Photograph(in);
        }

        @Override
        public Photograph[] newArray(int size) {
            return new Photograph[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByteArray(image);
        dest.writeString(description);
    }
}

