/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 01.02.2018
 *
 * Model of the User Table.
 *
 */

package io.acube.acubeio.model;

import lombok.Data;

@Data
public class UserModel {
    private String id;
    private String accessToken;
    private String refreshToken;
}
