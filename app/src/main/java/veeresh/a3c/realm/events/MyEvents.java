package veeresh.a3c.realm.events;

import android.widget.ImageView;

import veeresh.a3c.realm.models.Record;

/**
 * Created by Veeresh on 3/11/17.
 */

public class MyEvents {


    public static class RecordEvent {
        Record record;
        ImageView imageView;


        public ImageView getImageView() {
            return imageView;
        }

        public RecordEvent(Record record, ImageView imageView){
            this.record = record;
            this.imageView = imageView;

        }

        public Record getRecord() {
            return record;
        }
    }
}
