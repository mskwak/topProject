package com.daou.controller;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.daou.UserSolrBean;

@Controller
public class SolrTest {

	@RequestMapping(value = "/solrUrl.do", method = {RequestMethod.GET})
	public void solrMethod(Model model) {
		HttpSolrClient httpSolrClient = new HttpSolrClient("http://172.21.29.187:8983/solr/gettingstarted");

		SolrQuery solrQuery = new SolrQuery();

		solrQuery.setRequestHandler("/select");
		solrQuery.set("q", "*:*");
		solrQuery.set("wt", "json");
		solrQuery.set("indent", true);

		try {
			QueryResponse queryResponse = httpSolrClient.query(solrQuery);
			SolrDocumentList solrDocumentList = queryResponse.getResults();

			for(SolrDocument solrDocument: solrDocumentList) {
				System.out.println(solrDocument.toString());
			}

			model.addAttribute("solrList", solrDocumentList);

		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(httpSolrClient);
		}
	}

	@RequestMapping(value = "/solrJson.do", method = {RequestMethod.GET})
	public ModelAndView solrJsonMethod(ModelAndView modelAndView) {
		HttpSolrClient httpSolrClient = new HttpSolrClient("http://172.21.29.187:8983/solr/gettingstarted");

		SolrQuery solrQuery = new SolrQuery();

		solrQuery.setRequestHandler("/select");
		solrQuery.set("q", "*:*");
		solrQuery.set("wt", "json");
		solrQuery.set("indent", true);

		try {
			QueryResponse queryResponse = httpSolrClient.query(solrQuery);
			SolrDocumentList solrDocumentList = queryResponse.getResults();

			for(SolrDocument solrDocument: solrDocumentList) {
				System.out.println(solrDocument.toString());
			}

			modelAndView.setViewName("jsonView");
			modelAndView.addObject("solrList", solrDocumentList);

		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(httpSolrClient);
		}

		return modelAndView;
	}

	@RequestMapping(value = "/solrPing.do", method = {RequestMethod.GET})
	public String solrPingGet() throws SolrServerException, IOException {
		//String urlString = "http://172.21.29.187:8983/solr/multicore/language";
		String urlString = "http://172.21.29.187:8983/solr/user";
		SolrClient solrClient = new HttpSolrClient(urlString);

		SolrPingResponse solrPingResponse = solrClient.ping();

		System.out.println(solrPingResponse.toString());
		System.out.println(solrPingResponse.getStatus());

		UserSolrBean userSolrBean = new UserSolrBean();

		userSolrBean.setCompanyId(1L);
		userSolrBean.setCreatedAt(new Date());
		userSolrBean.setEmail("mskw@daou.com");
		userSolrBean.setId(100L);
		userSolrBean.setName("행국이");
		userSolrBean.setStatus("enable");
		userSolrBean.setUpdatedAt(new Date());

		org.apache.solr.client.solrj.response.UpdateResponse updateResponse = solrClient.addBean(userSolrBean);
		System.out.println("xxxxxxxxxxxx:" + updateResponse.toString());

		solrClient.commit();
		solrClient.close();

		return "solrPing";
	}

	@RequestMapping(value = "/solrSearch.do", method = {RequestMethod.GET})
	public String solrSearchGet() throws SolrServerException, IOException {
		//String urlString = "http://172.21.29.187:8983/solr/multicore/language";
		String urlString = "http://172.21.29.187:8983/solr/user";
		SolrClient solrClient = new HttpSolrClient(urlString);

		SolrPingResponse solrPingResponse = solrClient.ping();

		System.out.println(solrPingResponse.toString());
		System.out.println(solrPingResponse.getStatus());

		SolrQuery solrQuery = new SolrQuery();

		solrQuery.setQuery("mskw");

		QueryResponse queryResponse = solrClient.query(solrQuery);

		System.out.println("xxxxxxxxxxxx:" + queryResponse.toString());

		solrClient.close();

		return "solrSearch";
	}
}
