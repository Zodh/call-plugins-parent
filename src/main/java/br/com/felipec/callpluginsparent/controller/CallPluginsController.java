package br.com.felipec.callpluginsparent.controller;

import br.com.felipec.callpluginsparent.api.ResultsApi;
import br.com.felipec.callpluginsparent.model.Results;
import br.com.felipec.callpluginsparent.service.CallPluginsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class CallPluginsController implements ResultsApi {

  @Autowired
  private CallPluginsService callPluginsService;

  public ResponseEntity<Results> resultsPost(Integer messagesQuantity) {
    return new ResponseEntity<>(
        callPluginsService.testPlugins(messagesQuantity),
        HttpStatus.OK
    );
  }
}
