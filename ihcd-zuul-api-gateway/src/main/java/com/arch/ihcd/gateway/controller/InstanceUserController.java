package com.arch.ihcd.gateway.controller;


import com.arch.ihcd.gateway.request.InstanceCreationRequest;
import com.arch.ihcd.gateway.request.InstanceUserCreationRequest;
import com.arch.ihcd.gateway.response.GenericResponse;
import com.arch.ihcd.gateway.service.instance.IInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Api(tags = {"API Instance Controller"})
@SwaggerDefinition(tags = {
        @Tag(name = "Instance Controller", description = "Instance details")
})
@CrossOrigin
@RestController
@RequestMapping("api/instances")
public class InstanceUserController {

    @Autowired
    private IInstanceService instanceService;

    @ApiOperation(value = "New Instance Registration", response = ResponseEntity.class)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> registerInstance(@RequestBody InstanceCreationRequest request) {
        return new ResponseEntity<GenericResponse>(new GenericResponse(instanceService.registerInstance(request)), HttpStatus.OK);
    }

    @ApiOperation(value = "New Instance User Registration", response = ResponseEntity.class)
    @PostMapping(value= "/newUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerInstanceUser(@RequestBody InstanceUserCreationRequest request) {
        return new ResponseEntity<>(new GenericResponse(instanceService.registerInstanceUser(request)), HttpStatus.OK);
    }

}
