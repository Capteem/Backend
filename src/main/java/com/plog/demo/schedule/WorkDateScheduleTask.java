package com.plog.demo.schedule;

import com.plog.demo.model.WorkdateTable;
import com.plog.demo.repository.WorkdateTableRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;


@Slf4j
@Component
public class WorkDateScheduleTask {

    @Autowired
    private WorkdateTableRepository workdateTableRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void updateWorkdatesToTargetMonth() {
        LocalDateTime now = LocalDateTime.now();

        // 오늘 이전의 날짜들 중에서 동일한 주의 동일한 요일로 변경할 날짜를 찾기
        List<WorkdateTable> workdatesToUpdate = workdateTableRepository.findByWorkDateBefore(now);

        for (WorkdateTable workdate : workdatesToUpdate) {
            LocalDateTime workDateTime = workdate.getWorkDate();

            // 두 달 후의 같은 주의 같은 요일 계산
            LocalDateTime targetDateTime = getTargetDateTimeOneMonthsLater(workDateTime);

            workdate.setWorkDate(targetDateTime); // 시간 유지
        }
        workdateTableRepository.saveAll(workdatesToUpdate);

        System.out.println("Updated workdates to two months later: " + now.plusMonths(2).getMonthValue());
    }

    private LocalDateTime getTargetDateTimeOneMonthsLater(LocalDateTime dateTime) {
        LocalDateTime twoMonthsLater = dateTime.plusMonths(1);

        // 두 달 후의 같은 주의 동일한 요일 계산
        LocalDateTime firstDayOfTargetMonth = LocalDateTime.of(twoMonthsLater.getYear(), twoMonthsLater.getMonth(), 1, 0, 0);

        // 같은 주의 동일한 요일을 계산하기 위해 TemporalAdjusters 사용
        LocalDateTime targetDateTime = firstDayOfTargetMonth.with(TemporalAdjusters.dayOfWeekInMonth(
                (dateTime.getDayOfMonth() - 1) / 7 + 1, // 주차 계산
                dateTime.getDayOfWeek() // 요일
        ));

        // 시간 유지
        return targetDateTime.withHour(dateTime.getHour()).withMinute(dateTime.getMinute()).withSecond(dateTime.getSecond());
    }
}
