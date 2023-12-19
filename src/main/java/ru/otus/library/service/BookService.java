package ru.otus.library.service;

import jakarta.servlet.http.HttpSession;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.otus.library.dao.BooksRepository;
import ru.otus.library.data.Approval;
import ru.otus.library.entity.Book;
import ru.otus.library.security.LibraryOidcUser;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private BooksRepository repository;

    @Value("${server.truststore.store}")
    private Resource trustStore;

    @Value("${server.truststore.password}")
    private String trustStorePassword;

    @Autowired
    public BookService(BooksRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER')")
    public List<Book> getBooks() {
        List<Book> result = new ArrayList<>();
        repository.findAll().forEach(result::add);
        return result;
    }

    @Transactional
    public void addBook(String name) {
        Book book = new Book();
        book.setName(name);
        repository.save(book);
    }

    @Transactional
    public void removeBook(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void reserveBook(Long id) throws Exception{
        Optional<Book> book = repository.findById(id);
        if (book.isPresent() && checkApproval(book.get().id)) {
            Book entity = book.get();
            entity.setBooked(true);
            repository.save(entity);
        }
    }

    private boolean checkApproval(Long id) throws Exception {
        File truststoreFile = new File(".", trustStore.getFilename());

        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(truststoreFile, trustStorePassword.toCharArray())
                .build();

        SSLConnectionSocketFactory sslConFactory = new SSLConnectionSocketFactory(sslContext);

        HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslConFactory).build();

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        RestTemplate template = new RestTemplate(requestFactory);

        SecurityContext contextHolder = SecurityContextHolder.getContext();
        LibraryOidcUser user = (LibraryOidcUser) contextHolder.getAuthentication().getPrincipal();
        String token = user.getAccessToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        RequestEntity<Approval> requestEntity = new RequestEntity<>
                (headers, HttpMethod.GET, new URI("https://localhost:8181/approval?id=" + id));

        ResponseEntity<Approval> result = template.exchange(requestEntity, Approval.class);

        return result.getBody().approved();
    }

    @Transactional
    public void returnBook(Long id) {
        Optional<Book> book = repository.findById(id);
        if (book.isPresent()) {
            Book entity = book.get();
            entity.setBooked(false);
            repository.save(entity);
        }
    }
}
