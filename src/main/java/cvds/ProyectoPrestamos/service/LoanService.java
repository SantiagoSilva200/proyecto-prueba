package cvds.ProyectoPrestamos.service;

import cvds.ProyectoPrestamos.model.Loans;
import cvds.ProyectoPrestamos.repository.loansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.util.Optional;

@Service
public class LoanService {
    @Autowired
    loansRepository repository;
    @Autowired
    private RestTemplate restTemplate;

    public Optional<Loans> deleteLoan(Long id){
        Optional<Loans> Loan = repository.findById(id);
        repository.deleteById(id);
        return Loan;
    }

    public String showAvailability(String id) {
        String url = "https://api.example.com/books/" + id; //url prueba

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            return jsonObject.getString("availability");
        } catch (RestClientException e) {
            return "Error al consultar la disponibilidad";
        }
    }


}
