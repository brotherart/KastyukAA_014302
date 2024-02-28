package com.autodocservice.controller;

import com.autodocservice.model.DocumentComments;
import com.autodocservice.model.Documents;
import com.autodocservice.model.Users;
import com.autodocservice.repo.DocumentCommentsRepo;
import com.autodocservice.repo.DocumentsRepo;
import com.autodocservice.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentsCont {

    @Autowired
    protected DocumentsRepo documentsRepo;
    @Autowired
    protected DocumentCommentsRepo documentCommentsRepo;
    @Autowired
    protected UsersRepo usersRepo;

    @GetMapping("/{documentId}")
    public ResponseEntity<?> document(@PathVariable Long documentId) {
        Documents document;
        try {
            document = documentsRepo.findById(documentId).orElseThrow();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> documentSearch(@RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "") String date) {
        return new ResponseEntity<>(documentsRepo.findAllByNameContainingAndDateContaining(name, date), HttpStatus.OK);
    }

    @DeleteMapping("/{documentId}/delete")
    public ResponseEntity<?> documentDelete(@PathVariable Long documentId) {
        try {
            documentsRepo.findById(documentId).orElseThrow();
            documentsRepo.deleteById(documentId);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{documentId}/comment")
    public ResponseEntity<?> documentComment(@PathVariable Long documentId, @RequestBody DocumentComments comments, @RequestParam Long userId) {
        try {
            Documents document = documentsRepo.findById(documentId).orElseThrow();
            Users user = usersRepo.findById(userId).orElseThrow();

            if (document.getReceiver() == null) {
                return new ResponseEntity<>("Данный документ еще не отправлен", HttpStatus.BAD_REQUEST);
            }

            if (!document.getOwner().getId().equals(userId)) {
                if (!document.getReceiver().getId().equals(userId)) {
                    return new ResponseEntity<>("У данного пользователя нету доступа к переписке", HttpStatus.BAD_REQUEST);
                }
            }

            if (comments.getDate().isEmpty() || comments.getText().isEmpty()) {
                throw new Exception();
            }

            comments.setOwner(user);
            comments.setDocument(document);
            comments = documentCommentsRepo.saveAndFlush(comments);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/{documentId}/transfer")
    public ResponseEntity<?> documentTransfer(@PathVariable Long documentId, @RequestParam Long userId) {
        Documents document;
        try {
            Users user = usersRepo.findById(userId).orElseThrow();
            document = documentsRepo.findById(documentId).orElseThrow();
            if (document.getReceiver() != null) {
                return new ResponseEntity<>("У данного документа уже есть получатель", HttpStatus.BAD_REQUEST);
            }
            if (document.getOwner().getId().equals(userId)) {
                return new ResponseEntity<>("Получатель не может быть отправителем", HttpStatus.BAD_REQUEST);
            }
            document.setReceiver(user);
            document = documentsRepo.saveAndFlush(document);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> documentAdd(@RequestBody Documents document, @RequestParam Long userId) {
        try {
            Users user = usersRepo.findById(userId).orElseThrow();
            if (document.check()) {
                throw new Exception();
            }
            if (documentsRepo.findByNumber(document.getNumber()) != null) {
                return new ResponseEntity<>("Некорректный регистрационный номер", HttpStatus.BAD_REQUEST);
            }
            document.setOwner(user);
            document = documentsRepo.saveAndFlush(document);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @PutMapping("/{documentId}/edit")
    public ResponseEntity<?> documentEdit(
            @PathVariable Long documentId, @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int number, @RequestParam(defaultValue = "") String date,
            @RequestParam(defaultValue = "") String documentScan, @RequestParam(defaultValue = "") String document,
            @RequestParam(defaultValue = "") String description) {
        Documents documents;
        try {
            documents = documentsRepo.findById(documentId).orElseThrow();
            if (documentsRepo.findByNumber(number) != null) {
                return new ResponseEntity<>("Некорректный регистрационный номер", HttpStatus.BAD_REQUEST);
            }
            if (!name.isEmpty()) documents.setName(name);
            if (number > 0) documents.setNumber(number);
            if (!date.isEmpty()) documents.setDate(date);
            if (!documentScan.isEmpty()) documents.setDocumentScan(documentScan);
            if (!document.isEmpty()) documents.setDocument(document);
            if (!description.isEmpty()) documents.setDescription(description);
            documents = documentsRepo.saveAndFlush(documents);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }
}
