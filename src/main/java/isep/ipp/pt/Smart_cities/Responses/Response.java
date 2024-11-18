package isep.ipp.pt.Smart_cities.Responses;

public record Response<T>(String message, boolean success, Exception e, T data) {
    public static <T> Response success(String message, T data) {
        return new Response(message, true, null, data);
    }

    public static <T> Response success(String message) {
        return new Response(message, true, null, null);
    }

    public static <T> Response error(String message, Exception e, T data) {
        return new Response(message, false, e, data);
    }

    public static <T> Response error(String message, Exception e) {
        return new Response(message, false, e, null);
    }

    public Response {
    }

}
