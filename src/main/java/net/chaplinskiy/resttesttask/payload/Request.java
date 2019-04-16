package net.chaplinskiy.resttesttask.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class Request {
    @NotBlank
    @Size(max = 20)
    private String name;
}
