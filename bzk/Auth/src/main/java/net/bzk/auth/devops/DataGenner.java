package net.bzk.auth.devops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.Builder;
import net.bzk.auth.dao.AccountDao;
import net.bzk.auth.dao.PromoteTileDao;
import net.bzk.auth.dto.UserDto;
import net.bzk.auth.model.Account;
import net.bzk.auth.model.Bill;
import net.bzk.auth.model.Cosign;
import net.bzk.auth.model.EndUser;
import net.bzk.auth.model.Payer;
import net.bzk.auth.model.PromoteTile;
import net.bzk.auth.model.Account.Authority;
import net.bzk.auth.model.Bill.State;
import net.bzk.auth.model.UserBill.TransType;
import net.bzk.auth.service.AccountService;
import net.bzk.auth.service.CosignService;
import net.bzk.auth.service.EndUserService;
import net.bzk.auth.service.PayerService;
import net.bzk.infrastructure.CommUtils;
import net.bzk.infrastructure.JsonUtils;

@Service
@Profile({"dev","prod"})
public class DataGenner {

	@Inject
	private EndUserService endUserService;
	@Inject
	private AccountService accountService;
	@Inject
	private PayerService payerService;
	@Inject
	private AccountDao accountDao;
	@Inject
	private CosignService cosignService;
	@Inject
	private PromoteTileDao promoteTileDao;

	private Map<String, Object> userOMap = new HashMap<>();
	private Map<DemoAccountE, Account> accMap = new HashMap<>();

	private List<Bill> bills = new ArrayList<>();
	private List<Cosign> cosigns = new ArrayList<>();

	@Transactional
	@PostConstruct
	void init() {
		CommUtils.pl("DataGenner init");
		createAccounts();
		createBills();
		createCosign();
		CommUtils.pl(JsonUtils.toJson(userOMap));
		CommUtils.pl(JsonUtils.toJson(accMap));
		CommUtils.pl(JsonUtils.toJson(bills));
		CommUtils.pl(JsonUtils.toJson(cosigns));
		CommUtils.pl("DataGenner init DONE");
	}

	private void createBills() {
		Payer p = getUserO(DemoAccountE.Payer1, Authority.Payer);

		bills.add(createDoneBill(p,
				BillB.builder()
				.name("鈭砍��銵�")
				.tileImg("https://docs.google.com/drawings/d/e/2PACX-1vQzVEeJonvrEmfgiVD-X8UIsX2F6e8u8qWKLVwFUQ21G7mFnwC2CkzMe7skuq4KoYO4GQQheaaP0hcL/pub?w=960&h=720")
				.officialLink("https://netbank.ktb.com.tw/KTBPIB/WebApi/www/")
				.build()));
		bills.add(createDoneBill(p,
				BillB.builder()
				.name("����銵�")
				.tileImg("https://docs.google.com/drawings/d/e/2PACX-1vRUm6qFuN785YHKGbuH2biqbH88Zt48n-amo2__spq-5PLxb5-MqM5I8SGDlN77igOHvpOk4UlVoMlE/pub?w=977&h=547")
				.officialLink("https://www.kgibank.com/eventWeb/2018/MobileBanking/overview.html")
				.build()));
		bills.add(createDoneBill(p,
				BillB.builder()
				.name("����銵�")
				.tileImg("https://docs.google.com/drawings/d/e/2PACX-1vR67MS9m7O1N6-Buj6YO_vNCVdLN5kKbPl7Zo2A5eUAez1j9mfMnN3I1im5ahm6qLbDC_J6c0lGE4TA/pub?w=541&h=722")
				.officialLink("https://www.taishinbank.com.tw/TSB/personal/credit/online-apply/e-bill/")
				.build()));
		bills.add(createDoneBill(p,
				BillB.builder()
				.name("瘞貉��銵�")
				.tileImg("https://docs.google.com/drawings/d/e/2PACX-1vRDjEyckpeDA_6o2tgqZ1RXf458l4krcGo4CIxy_baFxhSnHyFxWmOJe06T99hNh_LQ_bDkso1vzw-N/pub?w=960&h=720")
				.officialLink("https://bank.sinopac.com/sinopacBT/personal/credit-card/discount/433750413.html")
				.build()));
		bills.add(createDoneBill(p,
				BillB.builder()
				.name("����銵�")
				.tileImg("https://docs.google.com/drawings/d/e/2PACX-1vSoVEYFWE8g_3SeGzORcQCSHW1xFeclAM15L1P0cXB99EMYHf_zeEgQGgM8Mg_XOWJl1lMUP7yYURtx/pub?w=949&h=468")
				.officialLink("https://card.hncb.com.tw/wps/portal/card/!ut/p/z1/04_Sj9CPykssy0xPLMnMz0vMAfIjo8ziDfxNPT0M_A18DYLDjAwcfSxNzL19DY3cTcz1wyEKcABHA_0oovTjVhCF3_hw_ShCSgpyQyMM0hUVAf5peGY!/?1dmy&urile=wcm%3Apath%3A/wps/wcm/connect/hncb/site_map/hncb/card/applications")
				.build()));

		createPromoteTile(bills.get(0));
		createPromoteTile(bills.get(1));
		createPromoteTile(bills.get(2));

		bills.add(createZeroBill(p,
				BillB.builder().name("皜����銵�").tileImg("https://docs.google.com/drawings/d/e/2PACX-1vSeTqa2wo0JYUg0i0n6ApVGIGK2PMAncu5mJojb0nsznlvPE6ZWwbx_w2OiPTFVN6YbqvODPCgg6cdr/pub?w=537&h=719").build()));
		bills.add(createZeroBill(p,
				BillB.builder().name("��控��銵�").tileImg("https://docs.google.com/drawings/d/e/2PACX-1vQBppf-cr1uYXvGiopPBkuottESPAnTplM3yWP4zZPBMEytXeHj3sjjH_XqDpWtsKVRJzLNpqtvnVBT/pub?w=960&h=720").build()));
		bills.add(createZeroBill(p,
				BillB.builder().name("�靽⊿�銵�").tileImg("https://docs.google.com/drawings/d/e/2PACX-1vSgv2oQexca3pXWcwM5HM_xgr8YbKvrDATHQKWJ0QTXr4Pnw6CFwbaL5NR9Y1ec45bgBmBdSb7Q_Rnt/pub?w=961&h=540").build()));
		bills.add(createZeroBill(p,
				BillB.builder().name("����銵�").tileImg("https://docs.google.com/drawings/d/e/2PACX-1vSXLFJVd92AGCD4gDT4FVOtJ3lZrJ149CSPZ3g6x30JBLjtFPX3Z5UECorWpgE24HoySPp51MAECC_v/pub?w=959&h=541").build()));
		bills.add(createZeroBill(p,
				BillB.builder().name("����銵�").tileImg("https://docs.google.com/drawings/d/e/2PACX-1vQM8iYGiTIQAmgq4g4qdpTwIzAxdKtp3ScXTSBs_Vam2kA9cCraE35Ks8VdF29S2-BYdXlr8qKYebAR/pub?w=960&h=720").build()));

		Bill zb = bills.stream().filter(b -> b.getState() == State.Zero).findAny().get();
		createPromoteTile(zb);

	}
	
	private void createPromoteTile(Bill b) {
		PromoteTile pt1 = new PromoteTile();
		pt1.setBillOid(b.getUid());
		pt1.setDescription("Description:"+b.getName());
		pt1.setTitle(b.getName());
		pt1.setTopText("TopText");
		pt1.setSubTitle("sub:"+b.getName());
		promoteTileDao.save(pt1);
	}

	@Builder
	public static class BillB {
		private String name;
		private String tileImg;
		private String officialLink;
	}

	private Bill createZeroBill(Payer p, BillB db) {
		return createBill(p, State.Zero, db);
	}

	private Bill createDoneBill(Payer p, BillB db) {
		return createBill(p, State.Done, db);
	}

	private Bill createBill(Payer p, State s, BillB db) {
		Bill nb = new Bill();
		nb.setPayerOid(p.getUid());
		nb.setName(db.name);
		nb.setState(s);
		nb.setTileImg(db.tileImg);
		nb.setOfficialLink(db.officialLink);
		nb = payerService.createBill(p.getUid(), nb);
		return nb;
	}

	private void createCosign() {
		Account sa = accountDao.findByUsername(DemoAccountE.EndUser1.toString()).get();
		Cosign ic = new Cosign();
		ic.setBillOid(bills.get(0).getUid());
		EndUser eu = getUserO(DemoAccountE.EndUser1, Authority.EndUser);
		ic.setEndUserOid(eu.getUid());
		ic.setTransType(TransType.Domi);
		cosigns.add(cosignService.create(sa, ic).getCosign());
	}

	private void createAccounts() {
		for (DemoAccountE dae : DemoAccountE.values()) {
			UserDto ud = new UserDto();
			ud.setUsername(dae.name());
			ud.setPassword("password");
			ud.setEmail(dae+"@dev.com");
			Account a = accountService.save(ud);
			accMap.put(dae, a);
			if (dae.hasAuthority(Authority.EndUser)) {
				putUserO(dae, Authority.EndUser, endUserService.becomeEndUser(a,eu->{}).getEndUser());
			}
			if (dae.hasAuthority(Authority.Admin)) {
				a.getAuthorities().add(Authority.Admin);
				accountDao.save(a);
			}
			if (dae.hasAuthority(Authority.Payer)) {
				putUserO(dae, Authority.Payer, payerService.become(a.getUid()));
			}
		}
	}

	private void putUserO(DemoAccountE de, Authority a, Object o) {
		userOMap.put(de.toString() + a.toString(), o);
	}

	private <T> T getUserO(DemoAccountE de, Authority a) {
		return (T) userOMap.get(de.toString() + a.toString());
	}

}
