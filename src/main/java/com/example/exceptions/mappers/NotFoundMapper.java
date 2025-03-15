package com.example.exceptions.mappers;

import com.example.exceptions.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundMapper implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException notFound) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(notFound.getMessage())
                .build();
    }

}
