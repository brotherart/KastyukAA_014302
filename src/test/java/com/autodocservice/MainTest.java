package com.autodocservice;

import com.autodocservice.model.DocumentComments;
import com.autodocservice.model.Documents;
import com.autodocservice.model.Users;
import com.autodocservice.model.enums.Role;
import com.autodocservice.repo.DocumentCommentsRepo;
import com.autodocservice.repo.DocumentsRepo;
import com.autodocservice.repo.UsersRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MainTest {

    @Autowired
    protected DocumentsRepo documentsRepo;
    @Autowired
    protected DocumentCommentsRepo documentCommentsRepo;
    @Autowired
    protected UsersRepo usersRepo;

    @Test
    void DocumentAdd() {
        String name = "Name";
        int number = 123;
        String date = "2024-03-02";
        String documentScan = "documentScan";
        String document = "document";
        String description = "description";

        Documents newDocument = new Documents(name, number, date, documentScan, document, description);

        Assertions.assertEquals(name, newDocument.getName());
        Assertions.assertEquals(number, newDocument.getNumber());
        Assertions.assertEquals(date, newDocument.getDate());
        Assertions.assertEquals(documentScan, newDocument.getDocumentScan());
        Assertions.assertEquals(document, newDocument.getDocument());
        Assertions.assertEquals(description, newDocument.getDescription());

        newDocument = documentsRepo.saveAndFlush(newDocument);

        Assertions.assertNotNull(newDocument);
        Assertions.assertEquals(name, newDocument.getName());
        Assertions.assertEquals(number, newDocument.getNumber());
        Assertions.assertEquals(date, newDocument.getDate());
        Assertions.assertEquals(documentScan, newDocument.getDocumentScan());
        Assertions.assertEquals(document, newDocument.getDocument());
        Assertions.assertEquals(description, newDocument.getDescription());

        documentsRepo.deleteById(newDocument.getId());
    }

    @Test
    void DocumentSearch() {
        String name = "Name";
        int number = 123;
        String date = "2024-03-02";
        String documentScan = "documentScan";
        String document = "document";
        String description = "description";

        Documents newDocument = documentsRepo.saveAndFlush(new Documents(name, number, date, documentScan, document, description));

        Assertions.assertNotNull(newDocument);

        for (Documents i : documentsRepo.findAllByName(name)) {
            Assertions.assertEquals(newDocument.getName(), i.getName());
        }

        for (Documents i : documentsRepo.findAllByDate(date)) {
            Assertions.assertEquals(newDocument.getDate(), i.getDate());
        }

        documentsRepo.deleteById(newDocument.getId());
    }

    @Test
    void DocumentSend() {
        String name = "Name";
        int number = 123;
        String date = "2024-03-02";
        String documentScan = "documentScan";
        String document = "document";
        String description = "description";

        Documents newDocument = documentsRepo.saveAndFlush(new Documents(name, number, date, documentScan, document, description));

        Assertions.assertNotNull(newDocument);
        Assertions.assertNull(newDocument.getReceiver());

        Users receiver = usersRepo.saveAndFlush(new Users("username", "password", Role.CLIENT));
        newDocument.setReceiver(receiver);

        Assertions.assertNotNull(newDocument.getReceiver());

        newDocument = documentsRepo.saveAndFlush(newDocument);

        Assertions.assertNotNull(newDocument.getReceiver());

        documentsRepo.deleteById(newDocument.getId());
        usersRepo.deleteById(receiver.getId());
    }

    @Test
    void DocumentDelete() {
        String name = "Name";
        int number = 123;
        String date = "2024-03-02";
        String documentScan = "documentScan";
        String document = "document";
        String description = "description";

        Documents newDocument = documentsRepo.saveAndFlush(new Documents(name, number, date, documentScan, document, description));

        Assertions.assertNotNull(newDocument);
        Long id = newDocument.getId();

        documentsRepo.deleteById(newDocument.getId());

        Documents documentDelete = documentsRepo.findById(id).orElse(null);

        Assertions.assertNull(documentDelete);
    }

    @Test
    void DocumentEdit() {
        String nameOld = "Name";
        int numberOld = 123;
        String dateOld = "2024-01-01";
        String documentScanOld = "documentScan";
        String documentOld = "document";
        String descriptionOld = "description";

        Documents documentEdit = new Documents(nameOld, numberOld, dateOld, documentScanOld, documentOld, descriptionOld);

        Assertions.assertEquals(nameOld, documentEdit.getName());
        Assertions.assertEquals(numberOld, documentEdit.getNumber());
        Assertions.assertEquals(dateOld, documentEdit.getDate());
        Assertions.assertEquals(documentScanOld, documentEdit.getDocumentScan());
        Assertions.assertEquals(documentOld, documentEdit.getDocument());
        Assertions.assertEquals(descriptionOld, documentEdit.getDescription());

        documentEdit = documentsRepo.saveAndFlush(documentEdit);

        Assertions.assertNotNull(documentEdit);
        Assertions.assertEquals(nameOld, documentEdit.getName());
        Assertions.assertEquals(numberOld, documentEdit.getNumber());
        Assertions.assertEquals(dateOld, documentEdit.getDate());
        Assertions.assertEquals(documentScanOld, documentEdit.getDocumentScan());
        Assertions.assertEquals(documentOld, documentEdit.getDocument());
        Assertions.assertEquals(descriptionOld, documentEdit.getDescription());

        String nameNew = "NameEdit";
        int numberNew = 321;
        String dateNew = "2024-03-03";
        String documentScanNew = "documentScanEdit";
        String documentNew = "documentEdit";
        String descriptionNew = "descriptionEdit";

        documentEdit.set(nameNew, numberNew, dateNew, documentScanNew, documentNew, descriptionNew);

        documentEdit = documentsRepo.saveAndFlush(documentEdit);

        Assertions.assertNotNull(documentEdit);

        Assertions.assertNotEquals(nameOld, documentEdit.getName());
        Assertions.assertEquals(nameNew, documentEdit.getName());

        Assertions.assertNotEquals(numberOld, documentEdit.getNumber());
        Assertions.assertEquals(numberNew, documentEdit.getNumber());

        Assertions.assertNotEquals(dateOld, documentEdit.getDate());
        Assertions.assertEquals(dateNew, documentEdit.getDate());

        Assertions.assertNotEquals(documentScanOld, documentEdit.getDocumentScan());
        Assertions.assertEquals(documentScanNew, documentEdit.getDocumentScan());

        Assertions.assertNotEquals(documentOld, documentEdit.getDocument());
        Assertions.assertEquals(documentNew, documentEdit.getDocument());

        Assertions.assertNotEquals(descriptionOld, documentEdit.getDescription());
        Assertions.assertEquals(descriptionNew, documentEdit.getDescription());

        documentsRepo.deleteById(documentEdit.getId());
    }

    @Test
    void UserAdd() {
        String username = "username";
        String password = "password";
        Role role = Role.MANAGER;

        Users user = new Users(username, password, role);

        Assertions.assertEquals(username, user.getUsername());
        Assertions.assertNotEquals(password, user.getPassword());
        Assertions.assertEquals(role, user.getRole());

        user = usersRepo.saveAndFlush(user);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(username, user.getUsername());
        Assertions.assertNotEquals(password, user.getPassword());
        Assertions.assertEquals(role, user.getRole());

        usersRepo.deleteById(user.getId());
    }

    @Test
    void DocumentCommentAdd() {
        String text = "username";
        String date = "2024-05-13";

        DocumentComments comment = new DocumentComments(text, date);

        Assertions.assertEquals(text, comment.getText());
        Assertions.assertEquals(date, comment.getDate());

        comment = documentCommentsRepo.saveAndFlush(comment);

        Assertions.assertNotNull(comment);
        Assertions.assertEquals(text, comment.getText());
        Assertions.assertEquals(date, comment.getDate());

        documentCommentsRepo.deleteById(comment.getId());
    }
}