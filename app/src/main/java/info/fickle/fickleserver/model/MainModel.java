package info.fickle.fickleserver.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bharath on 03/11/16.
 */

public class MainModel {

    public String offerf;
    public Boolean live;
    public MainModel(String offerf, Boolean live){
        this.offerf=offerf;
        this.live=live;
    }
    public String getOfferf() {
        return offerf;
    }
    public Boolean getLive() {
        return live;
    }
    public void setOfferf(String offerf){
        this.offerf=offerf;
    }
    public void setLive(Boolean live){
        this.live=live;
    }

}
