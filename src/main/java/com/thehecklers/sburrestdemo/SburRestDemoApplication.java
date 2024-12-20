package com.thehecklers.sburrestdemo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class SburRestDemoApplication {

	public static void main(String[] args) {

        SpringApplication.run(SburRestDemoApplication.class, args);
	}

}

@RestController
@RequestMapping("/coffees")
class RestApiController {
	// 처리할 데이터
	// 필드
	private List<Coffee> coffees = new ArrayList<>();

	// 생성자
	public RestApiController() {
		coffees.addAll(List.of(
				new Coffee("Cafe Cereza"),
				new Coffee("Cafe Ganador"),
				new Coffee("Cafe Lareno"),
				new Coffee("Cafe Tres Pontas")
		));
	}

	// GET 방식으로 처리...
//	@RequestMapping(value = "/coffees", method = RequestMethod.GET)
	@GetMapping
	Iterable<Coffee> getCoffees() {
		return coffees;
	}
	/*
	GetMapping, PostMapping, PutMapping, PatchMapping, DeleteMapping
	각 메서드별로 동작하는 Mapping 어노테이션(@)

	*/
	// 특정 ID를 통해서 불러오기
	@GetMapping("/{id}")
	Optional<Coffee> getCoffee(@PathVariable String id) {	// @PathVariable은 경로값을 변수로..
		for (Coffee c : coffees) {
			if (c.getId().equals(id)) {
				return Optional.of(c);
			}
		}
		return Optional.empty();	// null
	}

	// POST 생성하기
	@PostMapping
//	Coffee postCoffee(@RequestParam("id") String id, @RequestParam("name") String name) {
//		coffees.add(new Coffee(id, name));
//		return new Coffee(id, name);
//	}
	Coffee postCoffee(@RequestBody Coffee coffee){
		// @RequestBody는 요청 패킷의 데이터 부분을 읽어 들임
		// 통신 요청 패킷은 크게 두부분으로 나뉨. header(상태및 기타 정보), body (전달할 Data)
		// 때문에 RequestBody에는 JSON 형식으로 데이터를 표시해야 함.
		coffees.add(coffee);
		return coffee;
	}

	// Put 생성
	@PutMapping("/{id}")
	ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee){	// HTTP 상태 전달 기능
		int coffeeIndex = -1; // coffees에 값이 없는 경우
		for (Coffee c : coffees) {
			if (c.getId().equals(id)) {	// coffees에 있는 객체중 id가 같은 coffee가 있다면,
				coffeeIndex = coffees.indexOf(c);	// coffee 객체의 인덱스값을 전달
				coffees.set(coffeeIndex, coffee);	// coffeeIndex와 전달받은 coffee 객체 정보로 수정
			}
		}
//		return (coffeeIndex == -1)		// coffees 목록에 없는 경우
//				? postCoffee(coffee)	// 없는 경우 추가
//				: coffee;				// 수정 성공
		return (coffeeIndex == -1)
				? new ResponseEntity<>(postCoffee(coffee), HttpStatus.CREATED)	// 없는 경우 추가
				: new ResponseEntity<>(coffee, HttpStatus.OK);					// 수정 성공

	}

	// Delete 만들기
	@DeleteMapping("/{id}")
	void deleteCoffee(@PathVariable String id) {
		coffees.removeIf(c -> c.getId().equals(id));
	}
}

// 도메인 생성 (Coffee)
class Coffee {
	// 필드
	private String id;
	private String name;

	// 생성자
	public Coffee(){}

//	@JsonCreator
	public Coffee(String id, String name) {
		this.id = id;
		this.name = name;
	}
	public Coffee(String name){
		this(UUID.randomUUID().toString(),name);
	}



	// Getter, Setter
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


}
