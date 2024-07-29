package OfferDraftPackage.entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Helper.Adverts.generateCompanyUrl;
import static Helper.Adverts.generateName;
import static Helper.GeoAndLang.getRandomKey;
import static Helper.Offers.*;
import static SQL.AdvertSQL.*;

public class OfferTracking {
    Integer offerId;
    String trackingUrl;
    String additionalMacro;
    String trafficbackUrl;
    Integer trackingDomainUrlId;
    String trackingDomainUrlName;
    String redirectType;
    Boolean allowDeeplinks;
    Boolean allowImpressions;
    List<LandingPage> landingPages;

    public static class LandingPage {
        Integer landingPageId;
        String landingPageTitle;
        String landingPageTrackingUrl;
        String landingPagePreviewUrl;
        String landingPageType;
        String landingPageUpdatedAt;

        public LandingPage() {
        }

        public void fillOfferLandingPageWithRandomDataForAPI() {
            this.landingPageTitle = generateName(3, TECHNOLOGY_WORDS);
            this.landingPageTrackingUrl =
                    "{clickid} or {click_id} - clickid_param_info" +
                    "{pid} - Partner ID" +
                    "{ip} - IP-address" +
                    "{geo} - Country code" +
                    "{sub1-8} - SubId #1-8" +
                    "{deeplink} - Deeplink" +
                    "{device_ua} - Device User Agent" +
                    "{offer_id} - Offer ID" +
                    "{rand} - Unique number" +
                    "{time} - Unix time" +
                    "{city} - City" +
                    "{time_petty} - Time of click committing in format H:i:s" +
                    "{date_only} - Date of click committing in format Y-m-d" +
                    "{affiliate_name} - Company name of affiliate" +
                    "{advertiser_id} - ID of advertiser" +
                    "{offer_name} - Offer title" +
                    "{referrer} - Сlick referrer";
            this.landingPagePreviewUrl = generateCompanyUrl(this.landingPageTitle);
            this.landingPageType = getRandomKey(LANDING_PAGE_TYPE);
        }

        public Integer getLandingPageId() {
            return landingPageId;
        }

        public void setLandingPageId(Integer landingPageId) {
            this.landingPageId = landingPageId;
        }

        public String getLandingPageTitle() {
            return landingPageTitle;
        }

        public void setLandingPageTitle(String landingPageTitle) {
            this.landingPageTitle = landingPageTitle;
        }

        public String getLandingPageTrackingUrl() {
            return landingPageTrackingUrl;
        }

        public void setLandingPageTrackingUrl(String landingPageTrackingUrl) {
            this.landingPageTrackingUrl = landingPageTrackingUrl;
        }

        public String getLandingPagePreviewUrl() {
            return landingPagePreviewUrl;
        }

        public void setLandingPagePreviewUrl(String landingPagePreviewUrl) {
            this.landingPagePreviewUrl = landingPagePreviewUrl;
        }

        public String getLandingPageType() {
            return landingPageType;
        }

        public void setLandingPageType(String landingPageType) {
            this.landingPageType = landingPageType;
        }

        public String getLandingPageUpdatedAt() {
            return landingPageUpdatedAt;
        }

        public void setLandingPageUpdatedAt(String landingPageUpdatedAt) {
            this.landingPageUpdatedAt = landingPageUpdatedAt;
        }
    }

    public OfferTracking() {
    }

    public void fillOfferTrackingWithRandomDataForAPI() throws Exception {
        this.trackingUrl = generateCompanyUrl(generateName(2, TECHNOLOGY_WORDS));
        this.additionalMacro = "sub3={clickid_here}";
        this.trafficbackUrl = generateCompanyUrl(generateName(2, TECHNOLOGY_WORDS));
        this.trackingDomainUrlId = Integer.valueOf(getRandomValueFromBDWhere("id","tracking_domain",
                "domain_type", "3SNET"));
        this.redirectType = getRandomKey(REDIRECT_TYPE);
        this.allowDeeplinks = new Random().nextBoolean();
        this.allowImpressions = new Random().nextBoolean();

        List<LandingPage> landingPages = new ArrayList<>();
        for (int i = 0; i <= new Random().nextInt(5) + 1; i++) {
            LandingPage landingPage = new LandingPage();
            landingPage.fillOfferLandingPageWithRandomDataForAPI();
            landingPages.add(landingPage);
        }
        this.landingPages = landingPages;
    }

    public Integer getOfferId() {
        return offerId;
    }

    public void setOfferId(Integer offerId) {
        this.offerId = offerId;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    public String getAdditionalMacro() {
        return additionalMacro;
    }

    public void setAdditionalMacro(String additionalMacro) {
        this.additionalMacro = additionalMacro;
    }

    public String getTrafficbackUrl() {
        return trafficbackUrl;
    }

    public void setTrafficbackUrl(String trafficbackUrl) {
        this.trafficbackUrl = trafficbackUrl;
    }

    public String getTrackingDomainUrlName() {
        return trackingDomainUrlName;
    }

    public void setTrackingDomainUrlName(String trackingDomainUrlName) {
        this.trackingDomainUrlName = trackingDomainUrlName;
    }

    public Integer getTrackingDomainUrlId() {
        return trackingDomainUrlId;
    }

    public void setTrackingDomainUrlId(Integer trackingDomainUrlId) {
        this.trackingDomainUrlId = trackingDomainUrlId;
    }

    public String getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(String redirectType) {
        this.redirectType = redirectType;
    }

    public Boolean getAllowDeeplinks() {
        return allowDeeplinks;
    }

    public void setAllowDeeplinks(Boolean allowDeeplinks) {
        this.allowDeeplinks = allowDeeplinks;
    }

    public Boolean getAllowImpressions() {
        return allowImpressions;
    }

    public void setAllowImpressions(Boolean allowImpressions) {
        this.allowImpressions = allowImpressions;
    }

    public List<LandingPage> getLandingPages() {
        return landingPages;
    }

    public void setLandingPages(List<LandingPage> landingPages) {
        this.landingPages = landingPages;
    }
}