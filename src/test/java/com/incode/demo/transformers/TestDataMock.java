package com.incode.demo.transformers;

public interface TestDataMock {

    String STRING_REMOVE_TRANSFORMER_REQUEST = """
				{
					"elements": [
					{
					  "value": "Hello, World! Привет, мир!",
					  "transformers": [
						{
						  "group": "StringTransformer",
						  "transformerId": "string-remove-transformer",
						  "parameters": ["[aeiou]"]
						},
						{
						  "group": "group2",
						  "transformerId": "transformer3",
						  "parameters": []
						}
					  ]
					  }
					]
                  }""";

    String STRING_REPLACE_TRANSFORMER_REQUEST = """
				{
					"elements": [
					{
					  "value": "Hello, World! Привет, мир!",
					  "transformers": [
						{
						  "group": "StringTransformer",
						  "transformerId": "string-replace-transformer",
						  "parameters": ["l","m"]
						},
						{
						  "group": "group2",
						  "transformerId": "transformer3",
						  "parameters": []
						}
					  ]
					  }
					]
                  }""";


    String STRING_TRANSLITERATION_TRANSFORMER_REQUEST = """
				{
					"elements": [
					{
					  "value": "Hello, World! Привет, мир!",
					  "transformers": [
						{
						  "group": "StringTransformer",
						  "transformerId": "string-transliteration-transformer",
						  "parameters": ["[aeiou]"]
						},
						{
						  "group": "group2",
						  "transformerId": "transformer3",
						  "parameters": []
						}
					  ]
					  }
					]
                  }""";

	String ALL_STRING_TRANSFORMERS_REQUEST = """
				{
					"elements": [
					{
					  "value": "Hello, World! Привет, мир!",
					  "transformers": [
					  {
						  "group": "StringTransformer",
						  "transformerId": "string-remove-transformer",
						  "parameters": ["[aeiou]"]
						},
					  {
						  "group": "StringTransformer",
						  "transformerId": "string-replace-transformer",
						  "parameters": ["l","m"]
						},
						{
						  "group": "StringTransformer",
						  "transformerId": "string-transliteration-transformer",
						  "parameters": ["[aeiou]"]
						}
					  ]
					  }
					]
                  }""";
}
