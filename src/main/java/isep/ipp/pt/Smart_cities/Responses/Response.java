package isep.ipp.pt.Smart_cities.Responses;

public record Response<T>(String message, int statusCode, T data) {


    public static <T> Response<T> ok(String message, T data) {
        return new Response<>(message, 200, data);
    }

    public static <T> Response<T> created(String message, T data) {
        return new Response<>(message, 201, data);
    }

    public static <T> Response<T> badRequest(String message) {
        return new Response<>(message, 400, null);
    }

    public static <T> Response<T> notFound(String message) {
        return new Response<>(message, 404, null);
    }
    public static <T> Response<T> notFound(String message, T data) {
        return new Response<>(message, 404, data);
    }

    public static <T> Response<T> unauthorized(String message) {
        return new Response<>(message, 401, null);
    }

    public static <T> Response<T> forbidden(String message) {
        return new Response<>(message, 403, null);
    }

    public static <T> Response<T> internalError(String message) {
        return new Response<>(message, 500, null);
    }


    public Response {
    }

}
