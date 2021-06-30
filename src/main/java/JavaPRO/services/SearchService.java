package JavaPRO.services;

import JavaPRO.Util.PersonToDtoMapper;
import JavaPRO.Util.PostToDTOMapper;
import JavaPRO.api.response.PersonsResponse;
import JavaPRO.api.response.PostResponse;
import JavaPRO.config.Config;
import JavaPRO.config.exception.NotFoundException;
import JavaPRO.model.DTO.Auth.AuthorizedPerson;
import JavaPRO.model.DTO.PostDTO;
import JavaPRO.model.Person;
import JavaPRO.model.PersonView;
import JavaPRO.model.Post;
import JavaPRO.repository.PersonRepository;
import JavaPRO.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class SearchService {

    private final PostRepository postRepository;
    private final PersonRepository personRepository;

    private final PersonToDtoMapper personToDtoMapper;
    private final PostToDTOMapper postToDTOMapper;

    public SearchService(PostRepository postRepository,
                         PersonRepository personRepository, PersonToDtoMapper personToDtoMapper,
                         PostToDTOMapper postToDTOMapper) {
        this.postRepository = postRepository;
        this.personRepository = personRepository;
        this.personToDtoMapper = personToDtoMapper;
        this.postToDTOMapper = postToDTOMapper;
    }

    public ResponseEntity<PersonsResponse> searchPeopleByProperties(
            String firstName,
            String lastName,
            Integer ageFrom,
            Integer ageTo,
            String country,
            String town
    ) {
        //Преобразуем пришедшее нечто к рабочему формату
        firstName = stringFix(firstName);
        lastName = stringFix(lastName);
        ageFrom = ageFromFix(ageFrom);
        ageTo = ageToFix(ageTo);
        country = stringFix(country);
        town = stringFix(town);

        List<PersonView> personFound = postRepository.findPersonsByProperties(firstName, lastName, ageFrom, ageTo, country, town);

        List<Person> personList = new ArrayList<>();

        personFound.forEach(personView -> {
            try {
                personList.add(viewToPerson(personView));
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        });

        List<AuthorizedPerson> personDTOS = new ArrayList<>();

        personList.forEach(person -> personDTOS.add(personToDtoMapper.convertToDto(person)));

        return ResponseEntity
                .ok(new PersonsResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        0,
                        0,
                        20,
                        personDTOS));
    }

    public ResponseEntity<PostResponse> searchPosts(String searchText,
                                                    Long dateFromLong,
                                                    Long dateToLong,
                                                    String searchAuthor) {

        stringFix(searchText);
        stringFix(searchAuthor);
        Date dateFrom = dateFromFix(dateFromLong);
        Date dateTo = dateToFix(dateToLong);

        List<Post> postList = postRepository.findPostsByProperties(searchText, dateFrom, dateTo, searchAuthor);

        List<PostDTO> postDTOList = new ArrayList();

        postList.forEach(post -> postDTOList.add(postToDTOMapper.convertToDTO(post)));
        postDTOList.forEach(postDTO -> postDTO.setLikes(postRepository.getLikes(postDTO.getId())));

        return ResponseEntity
                .ok(new PostResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        0,
                        0,
                        20,
                        postDTOList));
    }

    /** Внутренние методы доя обработки данных */

    //Конвертер строки из View в строку из таблицы
    private Person viewToPerson(PersonView personView) throws NotFoundException {

        if (personRepository.findById(personView.getId()).isEmpty()) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }
        return personRepository.findById(personView.getId()).get();
    }

    private String stringFix(String variable) {
        if (variable == null || variable.length() == 0) {
            variable = "";
        } else {
            variable = variable.toLowerCase(Locale.ROOT);
        }
        return variable;
    }

    private Integer ageFromFix(Integer ageFrom) {
        if (ageFrom == null) {
            return 0;
        }
        return ageFrom;
    }

    private Integer ageToFix(Integer ageTo) {
        if (ageTo == null) {
            return 200;
        }
        return ageTo;
    }

    private Date dateFromFix(Long dateFromLong) {
        Date dateFrom = null;

        if (dateFromLong != null) {
            dateFrom = new Date(dateFromLong);
        } else {
            //нижняя граница 18.06.1970, 07:36:56
            dateFrom = new Timestamp(14531816);
        }

        return dateFrom;
    }

    private Date dateToFix(Long dateToLong) {
        Date dateTo = null;

        if (dateToLong != null) {
            dateTo = new Timestamp(dateToLong);
        } else {
            //верхняя граница - сейчас, т.к. посты из будущего мы не видим
            dateTo = new Date();
        }

        return dateTo;
    }
}