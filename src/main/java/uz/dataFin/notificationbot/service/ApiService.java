package uz.dataFin.notificationbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import uz.dataFin.notificationbot.model.DateDTO;
import uz.dataFin.notificationbot.model.MessageDTO;
import uz.dataFin.notificationbot.model.ReportDTO;
import uz.dataFin.notificationbot.model.Users;
import uz.dataFin.notificationbot.repository.MarketRepository;
import uz.dataFin.notificationbot.repository.UserRepository;
import uz.dataFin.notificationbot.utils.Constant;
import uz.dataFin.notificationbot.utils.Security;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ApiService {
    private final MarketRepository marketRepository;
    private final UserRepository userRepository;
    private final BotService botService;

    public List<Users> getUsersByMarketUsername(String username) {
        List<Users> usersList = new ArrayList<>();
        Long marketId = marketRepository.getMarketIdByUsername(username);
        List<Long> usersByMarketId = marketRepository.getUsersByMarketId(marketId);
        usersByMarketId.forEach(id -> usersList.add(userRepository.findById(id).get()));
        return usersList;
    }

    public void sendMessageToUser(MessageDTO messageDTO, String username)  {
        botService.sendMessageToUser(messageDTO, username);
    }


    public ReportDTO wareHouse(DateDTO dateDTO) {
        RestTemplateBuilder restTemplate = new RestTemplateBuilder();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(Security.LOGIN, Security.PASSWORD, StandardCharsets.UTF_8);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            HttpEntity<DateDTO> entity = new HttpEntity<>(dateDTO, headers);

            ResponseEntity<byte[]> response = restTemplate
                    .setConnectTimeout(Duration.ofSeconds(60))
                    .setReadTimeout(Duration.ofSeconds(60))
                    .build()
                    .exchange(Constant.REQUEST_URI +"/bot/reports", HttpMethod.POST, entity, byte[].class);
            byte[] responseBody = response.getBody();
            Path path= Paths.get("REPORTS");
            path=utilService.checkPackage(path);
            Files.write(Paths.get(path.toFile().getAbsolutePath()+"/report."+dateDTO.getTypeFile()), Objects.requireNonNull(response.getBody()));
            return new File(path.toFile().getAbsolutePath()+"/report."+dateDTO.getTypeFile());
        }catch (Exception e){
            System.out.println(LocalDate.now()+" "+dateDTO.getClientId()+", "+", File qabul qilib olishda xatolik, fileService.getReports");
        }
        return null;
    }

}
