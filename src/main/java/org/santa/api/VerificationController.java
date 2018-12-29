package org.santa.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.santa.service.VerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/verify")
@Api(description = "Controller to manage participant address verification")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {

        this.verificationService = verificationService;
    }

    @ApiOperation(
            value = "Returns HTTP 200",
            notes = "Verifies a participants address"
    )
    @GetMapping(value = "/{id}/{code}")
    @ResponseStatus(HttpStatus.OK)
    public void verify(@PathVariable("id") String id,
                       @PathVariable("code") String code)
            throws Exception {

        verificationService.verify(id, code);
    }
}
