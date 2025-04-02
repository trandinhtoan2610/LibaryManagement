package BUS;

import DAL.AuthorBookDAL;
import DTO.AuthorBookDTO;
import java.util.List;

public class AuthorBookBUS {
    private AuthorBookDAL bDAL;
    public AuthorBookBUS(){
        bDAL = new AuthorBookDAL();
    }
       public List<AuthorBookDTO> getBookByAuthorID(Long authorID){
        return bDAL.findBookByAuthorID(authorID);
    } 
}
