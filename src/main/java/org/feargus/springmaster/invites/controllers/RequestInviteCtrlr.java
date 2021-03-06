package org.feargus.springmaster.invites.controllers;

import org.feargus.springmaster.invites.InviteRequestObj;
import org.feargus.springmaster.mail.Mailer;
import org.feargus.springmaster.utils.SystemVars;
import org.feargus.springmaster.utils.UtilVars;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RequestInviteCtrlr {

    private static final Logger log = LoggerFactory.getLogger(RequestInviteCtrlr.class);

    @RequestMapping(value = "/requestInvite", method = RequestMethod.GET)
    public String greetingForm(@RequestParam(value = "projectName", required = true) String requestedProj, Model model) {
	model.addAttribute("requestInvite", new InviteRequestObj());
	model.addAttribute("requestedProj", requestedProj);
	return "requestInvite";
    }

    @RequestMapping(value = "/requestInvite", method = RequestMethod.POST)
    public String requestInvite(@RequestParam(value = "projectName", required = true) String projectName,
	    @ModelAttribute InviteRequestObj invite, Model model) {

	log.info("Invite requested by: " + UtilVars.PII_START + invite.getUserEmail() + UtilVars.PII_END);

	try {
	    Mailer mailSender = new Mailer();
	    mailSender.sendMail(SystemVars.getInstance().getADMIN_EMAIL(), SystemVars.getInstance().getADMIN_EMAIL(),
		    generateInviteRequestMsgSubject(invite), generateInviteRequestMsgBody(invite));
	} catch (Exception e) {
	    log.info("Failed to email myself an invite request.");
	    log.info(e.getMessage());
	    return "redirect:/error";// TODO Error page or something here.....
	}

	log.info("Finished sending a mail requesting invite for: " + UtilVars.PII_START + invite.getUserEmail()
		+ UtilVars.PII_END);

	return "inviteGenerated";
    }

    private String generateInviteRequestMsgSubject(InviteRequestObj invite) {
	return "Invite Request for: " + invite.getProjectName();
    }

    private String generateInviteRequestMsgBody(InviteRequestObj invite) {
	String generatedEmailLink = generateInviteRequestConfirmationLink(invite);

	return "I would like to join please! :)\n\nUser: " + invite.getUserEmail() + "\nProject: "
		+ invite.getProjectName() + "\nInvite Generator URL: " + generatedEmailLink;
    }

    private String generateInviteRequestConfirmationLink(InviteRequestObj invite) {
	return SystemVars.getInstance().getROOT_URL() + "/generateInvite?userEmail=" + invite.getUserEmail();
    }
}
