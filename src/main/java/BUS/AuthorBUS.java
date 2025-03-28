package BUS;

import DAL.AuthorDAL;
import DTO.AuthorDTO;

import java.util.ArrayList;
import java.util.List;

public class AuthorBUS {
    AuthorDAL authorDAL;
    List<AuthorDTO> authorDTOList;

    public AuthorBUS(){
        authorDAL = new AuthorDAL();
        authorDTOList = new ArrayList<>();
    }

    public List<AuthorDTO> getAuthorList(){
        authorDTOList = authorDAL.findAll();
        return authorDTOList;
    }

    public void addAuthor(AuthorDTO author){
        authorDAL.create(author);
        authorDTOList.add(author);
    }

    public void deleteAuthor(AuthorDTO author){
        boolean success = authorDAL.delete(author.getId());
        if(success){
            authorDTOList.removeIf(a -> a.getId()==author.getId());
        }
    }

    public void updateAuthor(AuthorDTO author){
        boolean success = authorDAL.update(author);
        if(success){
            authorDTOList.replaceAll(a-> a.getId()==author.getId() ? author : a );
        }
    }

    public AuthorDTO findAuthorByID(AuthorDTO author){

        return authorDAL.findById(author.getId());
    }



}
