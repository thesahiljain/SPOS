%{
	#include <stdio.h>
	#include <stdlib.h>
	#include <string.h>
	char* symbols[50];
	char* types[50];
	int size = 0;
%}

letter [A-Z|a-z]
digit [0-9]
identifier {letter}({letter}|{digit})*
access_specifier public|private|protected
data_type int|float|char|boolean|short|byte|void
key_word class|static|import|this|main
relational_operator -|=

%%

{relational_operator} {printf("RELATIONAL_OPERATOR");}
{key_word} {printf("KEY_WORD");}
{data_type} {printf("DATA_TYPE");}
{access_specifier} {printf("ACCESS_SPECIFIER");}
{identifier}/[{] {printf("CLASS"); add(yytext, "class");}
{identifier}/[(] {printf("METHOD"); add(yytext, "method");}

%%

int main(){
	yyin = fopen("source.java", "r");
	yylex();
	yywrap();
}

int yywrap(){
	return 1;
}

void add(char* symbol, char* type){
	char* copy = (char*)malloc(strlen(symbol)+1);
	strcpy(copy, symbol);
	symbols[size] = copy;
	types[size] = type;
	size++;
}

void display(){
	int i;
	for(i = 0; i < size; i++)
		printf("%s\t%s\n", symbols[i], types[i]);
}
