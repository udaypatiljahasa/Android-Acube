/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 01.02.2018
 *
 * Model of the User Profile Table.
 *
 */


package io.acube.acubeio.model;

import lombok.Data;

@Data
public class UserProfileModel {
    private String id;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String countryCode;
    private String mobile;
    private String profilePic;
    private int gender;
    private String dob;
    private String email;
}
