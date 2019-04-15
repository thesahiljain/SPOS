%{
#include <stdio.h>
extern int yylex();
extern int yywrap();
extern char* yyin;
%}

%token ID DT SC COMMA NL NUM

%%

start : DT L SC NL {printf("Valid\n");}
L : L COMMA ID | ID {};

%%

int yyerror(char* str){
	printf("Error\n");
}

int main(){
	yyin = fopen("sample.cpp", "r");
	yyparse();
}
