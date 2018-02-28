package kth.id2216.fleamarket.Home;

/**
 * Created by Xiao on 19/02/18.
 */

public class Item {

    private String title;
    private String desc;
    private String price;
    private String image;

    public Item(){

    }

    public Item(String title, String desc, String price, String image) {
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
