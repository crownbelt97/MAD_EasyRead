package sg.edu.np.mad.mad_easyread;

public class BookDetails {
    private String title;

    //to get variable from other file
    public String getTitle() {
        return title;
    }

    // Setter

    private String [] authors;

    //to get variable from other file
    public String getAuthor(int position) {
        return authors[position];
    }

    private String book_image;

    //to get variable from other file
    public String getBook_Image() {
        return book_image;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    private double rating;

    public double getRating(){
        return rating;
    }

    private String format;

    public String getFormat(){
        return format;
    }

    private int length;

    public int getLength(){
        return length;
    }

    private String publisher;

    public String getPublisher(){
        return publisher;
    }

    private String release;

    public String getRelease(){
        return release;
    }

    private String category;

    public String getCategory(){
        return category;
    }


    public BookDetails(String title,String [] authors,String book_image,String description,double rating,String format,int length,String publisher,String release,String category) {
        this.title = title;
        this.authors = authors;
        this.book_image = book_image;
        this.description = description;
        this.rating = rating;
        this.format = format;
        this.length = length;
        this.publisher = publisher;
        this.release = release;
        this.category = category;
    }
}
