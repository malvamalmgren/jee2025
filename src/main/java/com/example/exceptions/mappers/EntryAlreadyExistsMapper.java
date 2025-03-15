package com.example.exceptions.mappers;

import com.example.exceptions.EntryAlreadyExistsException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class EntryAlreadyExistsMapper implements ExceptionMapper<EntryAlreadyExistsException> {
    @Override
    public Response toResponse(EntryAlreadyExistsException entryAlreadyExists) {
        return Response.status(Response.Status.CONFLICT)
                .entity(entryAlreadyExists.getMessage())
                .build();
    }

}
