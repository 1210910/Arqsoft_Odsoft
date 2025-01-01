package pt.psoft.g1.psoftg1.unitTests.authormanagement.model;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.services.generator.IdGeneratorFactory;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthorTest {
    private final String validName = "João Alberto";
    private final String validBio = "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.";
    private final UpdateAuthorRequest updateRequest = new UpdateAuthorRequest(validName, validBio, null, null);

    @BeforeEach
    void setUp() {
        // Setup necessário para todos os testes
    }

    // Teste para garantir que o nome não pode ser nulo
    @Test
    void ensureNameNotNull() {
        assertThrows(IllegalArgumentException.class, () -> new Author(null, validBio, null, null));
    }

    // Teste para garantir que a biografia não pode ser nula
    @Test
    void ensureBioNotNull() {
        assertThrows(IllegalArgumentException.class, () -> new Author(validName, null, null, null));
    }

    // Teste para verificar conflito de versão ao aplicar patch
//    @Test
//    void whenVersionIsStaleItIsNotPossibleToPatch() {
//        Author subject = new Author(validName, validBio, null, null);
//        assertThrows(StaleObjectStateException.class, () -> subject.applyPatch(999, updateRequest));
//    }

    // Teste para criação de autor sem foto
    @Test
    void testCreateAuthorWithoutPhoto() {
        Author author = new Author(validName, validBio, null, null);
        assertNotNull(author);
        assertNull(author.getPhoto());
        assertEquals(validName, author.getName());
        assertEquals(0,author.getVersion());
    }

    @Test
    void setAuthorNumber() {
        Author author = new Author(validName, validBio, null, null);
        author.setAuthorNumber("123");
        assertEquals("123", author.getAuthorNumber());
    }

    // Teste para criação de autor com foto
    @Test
    void testCreateAuthorRequestWithPhoto() {
        CreateAuthorRequest request = new CreateAuthorRequest(validName, validBio, null, "photoTest.jpg");
        Author author = new Author(request.getName(), request.getBio(), "photoTest.jpg", null);
        assertNotNull(author);
        assertEquals(request.getPhotoURI(), author.getPhoto().getPhotoFile());
    }

    // Teste para remoção de foto com conflito de versão
    @Test
    void testRemovePhotoWithVersionConflict() {
        Author author = new Author(validName, validBio, "photoTest.jpg", null);
        assertThrows(ConflictException.class, () -> author.removePhoto(999));
    }

//    // Teste para aplicar patch sem conflito de versão
//    @Test
//    void testApplyPatchWithoutConflict() {
//        Author author = new Author(validName, validBio, "photoTest.jpg", null);
//        author.applyPatch(0, new UpdateAuthorRequest("New Bio", "New Name", null, ""));
//        assertEquals("New Bio", author.getBio());
//        assertEquals("New Name", author.getName());
//        assertEquals("", author.getPhotoURI());
//    }

    // Teste para setGenId quando genId é nulo
    @Test
    void testSetGenIdWhenNull() {
        Author author = new Author(validName, validBio, null, null);
        author.setGenId(author.getGenId());
        assertNotNull(author.getGenId());
    }

    // Teste para setGenId quando genId já existe
    @Test
    void testSetGenIdWhenNotNull() {
        Author author = new Author(validName, validBio, null, "existingId");
        author.setGenId("newId");
        assertEquals("newId", author.getGenId());
    }

    // Teste para garantir que o nome é retornado corretamente
    @Test
    void testGetName() {
        Author author = new Author(validName, validBio, null, null);
        assertEquals(validName, author.getName());
    }

    // Teste para garantir que a biografia é retornada corretamente
    @Test
    void testGetBio() {
        Author author = new Author(validName, validBio, null, null);
        assertEquals(validBio, author.getBio());
    }

    // Teste para criar uma entidade com foto e garantir que foto é válida
    @Test
    void testEntityWithPhotoSetPhotoInternalWithValidURI() {
        EntityWithPhoto entity = new EntityWithPhotoImpl();
        String validPhotoURI = "photoTest.jpg";
        entity.setPhoto(validPhotoURI);
        assertNotNull(entity.getPhoto());
    }

    // Teste para garantir que a foto é opcional
    @Test
    void ensurePhotoCanBeNull_AkaOptional() {
        Author author = new Author(validName, validBio, null, null);
        assertNull(author.getPhoto());
    }

    // Teste para verificar que a foto é válida ao ser definida
    @Test
    void ensureValidPhoto() {
        Author author = new Author(validName, validBio, "photoTest.jpg", null);
        Photo photo = author.getPhoto();
        assertNotNull(photo);
        assertEquals("photoTest.jpg", photo.getPhotoFile());
    }

    private static class EntityWithPhotoImpl extends EntityWithPhoto {
    }
}

