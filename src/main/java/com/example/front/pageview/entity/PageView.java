package com.example.front.pageview.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "page_views")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageView {
    @EmbeddedId
    private Pk pk;

    @Column(name = "view_count")
    private Long viewCount;

    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "view_date")
        private LocalDate viewDate;

        @Convert(converter = PageTypeConverter.class)
        @Column(name = "page_id", length = 50)
        private PageType pageType;
    }
}
