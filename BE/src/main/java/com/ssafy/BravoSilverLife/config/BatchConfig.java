package com.ssafy.BravoSilverLife.config;

import com.ssafy.BravoSilverLife.entity.Bookmark;
import com.ssafy.BravoSilverLife.entity.User;
import com.ssafy.BravoSilverLife.repository.BookmarkRepository;
import com.ssafy.BravoSilverLife.repository.UserRepository;
import com.ssafy.BravoSilverLife.service.EstateService;
import com.ssafy.BravoSilverLife.service.EstateServiceImpl;
import com.ssafy.BravoSilverLife.service.MMSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final MMSService mmsService;

    private final EstateService estateService;

    private final BookmarkRepository bookmarkRepository;

    private final UserRepository userRepository;

    @Bean
    public Job job() {

        Job job = jobBuilderFactory.get("job")
                .start(step())
                .build();

        return job;
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step")
                .tasklet((contribution, chunkContext) -> {
                    log.info("Start!");
//                    User user;
//                    Bookmark bookmark;
                    List<User> list = userRepository.findAll();
                    for (User user : list) {
                        StringBuilder sb = new StringBuilder();
                        List<Bookmark> bmlist = bookmarkRepository.findByUser(user);
                        for (Bookmark bookmark : bmlist) {
                            sb.append(bookmark.getAddress()).append(" ").append(bookmark.getPrice()).append("/").append(bookmark.getMonth()).append(" ").append(bookmark.getUrl()).append(" ");
                        }
                        if(sb.length() > 2){
                            System.out.println(sb);
                            mmsService.sendBookMarkMMS(user.getPhoneNumber(), mmsService.userAuth(), String.valueOf(sb));

                        }

                    }



                    log.info("Finish!");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}