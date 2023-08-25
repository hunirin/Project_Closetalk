package team.closetalk.user.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailSendService {
    private final JavaMailSender javaMailSender;
    private static final String ADMIN_ADDRESS = "hun053@naver.com";

    private int authCode;

    public void makeAuthCode(){
        Random random = new Random();
        authCode = random.nextInt(888888) + 111111;
    }

    public String makeEmailAuth(String receiver){
        makeAuthCode();

        String title = "[CloseTalk] 클로젯톡 회원가입을 위한 인증 메일입니다.";

        String content = "귀하의 회원가입을 위한 이메일 인증번호입니다.\n"
                +authCode;

        sendEmail(receiver, title, content);

        return Integer.toString(authCode);

    }

    public void sendEmail(String receiver, String title, String content){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try{
            mimeMessage.addRecipients(Message.RecipientType.TO, receiver);
            mimeMessage.setSubject(title);
            mimeMessage.setText(content);
            mimeMessage.setFrom(new InternetAddress(ADMIN_ADDRESS, "CLOSETALK"));
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e){
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
