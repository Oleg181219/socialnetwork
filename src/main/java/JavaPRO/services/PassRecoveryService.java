package JavaPRO.services;

import JavaPRO.api.request.SetPasswordRequest;
import JavaPRO.api.response.OkResponse;
import JavaPRO.api.response.ResponseData;
import JavaPRO.config.Config;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.model.Person;
import JavaPRO.repository.PersonRepository;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PassRecoveryService {

    private final EmailService emailService;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger;
    @Value("${spring.mail.address}")
    private String address;

    public PassRecoveryService(EmailService emailService,
                               PersonRepository personRepository,
                               PasswordEncoder passwordEncoder,
                               @Qualifier("passRecoveryLogger") Logger logger) {
        this.emailService = emailService;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.logger = logger;
    }

    public ResponseEntity<OkResponse> passRecovery(String email) throws BadRequestException {
        if (email == null) {
            throw new BadRequestException(Config.STRING_AUTH_INVALID_EMAIL);
        }
        Person person = personRepository.findByEmail(email);
        if (person == null) {
            logger.error("Ошибка при восстановлении пароля. Пользователь с введенным email не найден. Email: " + email);
            throw new BadRequestException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        }
        String messageBody = "Hello, to recovery your password follow to link " +
                "<a href=\"" + address + "/change-password?token=" +
                person.getConfirmationCode() + "\">Password recovery</a>";
        emailService.sendMail("Recovery password in social network", messageBody, email);
        logger.info("Успешная отправка сообщения с ссылкой для восстановления пароля. Email: " + email);
        return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
    }

    public ResponseEntity<OkResponse> setNewPassword(SetPasswordRequest setPasswordRequest) throws BadRequestException {
        if (setPasswordRequest.getPassword() == null || setPasswordRequest.getToken() == null) {
            throw new BadRequestException(Config.STRING_BAD_REQUEST);
        }
        String token = setPasswordRequest.getToken();
        String password = setPasswordRequest.getPassword();
        Person person = personRepository.findByConfirmationCode(token);
        if (person == null) {
            logger.error("Ошибка при смене пароля. Не найдено пользователя по предоставленному токену. Token: " + setPasswordRequest.getToken());
            throw new BadRequestException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        } else {
            if (personRepository.setNewPassword(passwordEncoder.encode(password), token) != null) {
                logger.info("Успешная смена пароля. Email: " + person.getEmail());
                return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
            } else {
                logger.error("Ошибка при смене пароля. Ошибка при обработке запроса в БД. Email: " + person.getEmail());
                throw new BadRequestException(Config.STRING_INVALID_SET_PASSWORD);
            }
        }
    }

    private Long getTimestamp() {
        return (new Date().getTime() / 1000);
    }
}