package sg.edu.np.mad.easyread;

public class Book {

    private String title;

    //to get variable from other file
    public String getTitle() {
        return title;
    }

    private String book_image;

    //to get variable from other file
    public String getBook_Image() {
        return book_image;
    }

    private String details_link;

    //to get variable from other file
    public String getDetails_Link(){
        return details_link;
    }

    //Constructor
    public Book(String title,String book_image,String details_link) {
        this.title = title;
        this.book_image = book_image;
        this.details_link = details_link;
    }
//

}
