/*
 * Copyright 2015 Google Inc. All rights reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package Layer2.API.MapAPI;

import Layer2.API.MapAPI.gson.FieldNamingPolicy;
import Layer2.API.MapAPI.errors.ApiException;
import Layer2.API.MapAPI.internal.ApiConfig;
import Layer2.API.MapAPI.internal.ApiResponse;
import Layer2.API.MapAPI.internal.StringJoin;
import Layer2.API.MapAPI.internal.StringJoin.UrlValue;
import Layer2.API.MapAPI.model.PlaceDetails;

/**
 * A <a href="https://developers.google.com/places/web-service/details#PlaceDetailsRequests">Place
 * Details</a> request.
 */
public class PlaceDetailsRequest
    extends PendingResultBase<PlaceDetails, PlaceDetailsRequest, PlaceDetailsRequest.Response> {

  static final ApiConfig API_CONFIG =
      new ApiConfig("/maps/api/place/details/json")
          .fieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

  public PlaceDetailsRequest(GeoApiContext context) {
    super(context, API_CONFIG, Response.class);
  }

  /**
   * Specifies the Place ID to get Place Details for. Required.
   *
   * @param placeId The Place ID to retrieve details for.
   * @return Returns this {@code PlaceDetailsRequest} for call chaining.
   */
  public PlaceDetailsRequest placeId(String placeId) {
    return param("placeid", placeId);
  }

  /**
   * Sets the SessionToken for this request. Use this for Place Details requests that are called
   * following an autocomplete request in the same user session. Optional.
   *
   * @param sessionToken Session Token is the session identifier.
   * @return Returns this {@code PlaceDetailsRequest} for call chaining.
   */
  public PlaceDetailsRequest sessionToken(PlaceAutocompleteRequest.SessionToken sessionToken) {
    return param("sessiontoken", sessionToken);
  }

  /**
   * Sets the Region for this request. The region code, specified as a ccTLD (country code top-level
   * domain) two-character value. Most ccTLD codes are identical to ISO 3166-1 codes, with some
   * exceptions. This parameter will only influence, not fully restrict, results.
   *
   * @param region The region code.
   * @return Returns this {@code PlaceDetailsRequest} for call chaining.
   */
  public PlaceDetailsRequest region(String region) {
    return param("region", region);
  }

  /**
   * Specifies the field masks of the details to be returned by PlaceDetails.
   *
   * @param fields The Field Masks of the fields to return.
   * @return Returns this {@code PlaceDetailsRequest} for call chaining.
   */
  public PlaceDetailsRequest fields(FieldMask... fields) {
    return param("fields", StringJoin.join(',', fields));
  }

  @Override
  protected void validateRequest() {
    if (!params().containsKey("placeid")) {
      throw new IllegalArgumentException("Request must contain 'placeId'.");
    }
  }

  public static class Response implements ApiResponse<PlaceDetails> {
    public String status;
    public PlaceDetails result;
    public String[] htmlAttributions;
    public String errorMessage;

    @Override
    public boolean successful() {
      return "OK".equals(status) || "ZERO_RESULTS".equals(status);
    }

    @Override
    public PlaceDetails getResult() {
      if (result != null) {
        result.htmlAttributions = htmlAttributions;
      }
      return result;
    }

    @Override
    public ApiException getError() {
      if (successful()) {
        return null;
      }
      return ApiException.from(status, errorMessage);
    }
  }

  public enum FieldMask implements UrlValue {
    ADDRESS_COMPONENT("address_component"),
    ADR_ADDRESS("adr_address"),
    ALT_ID("alt_id"),
    FORMATTED_ADDRESS("formatted_address"),
    FORMATTED_PHONE_NUMBER("formatted_phone_number"),
    GEOMETRY("geometry"),
    ICON("icon"),
    ID("id"),
    INTERNATIONAL_PHONE_NUMBER("international_phone_number"),
    NAME("name"),
    OPENING_HOURS("opening_hours"),
    PERMANENTLY_CLOSED("permanently_closed"),
    PHOTOS("photos"),
    PLACE_ID("place_id"),
    PRICE_LEVEL("price_level"),
    RATING("rating"),
    REFERENCE("reference"),
    REVIEWS("reviews"),
    SCOPE("scope"),
    TYPES("types"),
    URL("url"),
    UTC_OFFSET("utc_offset"),
    VICINITY("vicinity"),
    WEBSITE("website");

    private final String field;

    FieldMask(final String field) {
      this.field = field;
    }

    @Override
    public String toUrlValue() {
      return field;
    }
  }
}