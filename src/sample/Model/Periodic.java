package sample.Model;

import java.util.Set;

public class Periodic {

    private String title;
    private String issn;
    private String eIssn;
    private String alternativeTitle;
    private String alternativeIssn;
    private String alternativeEIssn;
    private Long credit;

    private Set<Category> categories;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getEIssn() {
        return eIssn;
    }

    public void setEIssn(String eIssn) {
        this.eIssn = eIssn;
    }

    public String getAlternativeTitle() {
        return alternativeTitle;
    }

    public void setAlternativeTitle(String alternativeTitle) {
        this.alternativeTitle = alternativeTitle;
    }

    public String getAlternativeIssn() {
        return alternativeIssn;
    }

    public void setAlternativeIssn(String alternativeIssn) {
        this.alternativeIssn = alternativeIssn;
    }

    public String getAlternativeEIssn() {
        return alternativeEIssn;
    }

    public void setAlternativeEIssn(String alternativeEIssn) {
        this.alternativeEIssn = alternativeEIssn;
    }

    public Long getCredit() {
        return credit;
    }

    public void setCredit(Long credit) {
        this.credit = credit;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Periodic{" +
                "title='" + title + '\'' +
                ", issn='" + issn + '\'' +
                ", eIssn='" + eIssn + '\'' +
                ", alternativeTitle='" + alternativeTitle + '\'' +
                ", alternativeIssn='" + alternativeIssn + '\'' +
                ", alternativeEIssn='" + alternativeEIssn + '\'' +
                ", credit=" + credit +
                ", categories=" + categories +
                '}';
    }
}
