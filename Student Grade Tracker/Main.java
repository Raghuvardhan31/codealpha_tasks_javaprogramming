import java.util.Scanner ;
class Student{
    int n ;
    int sub ;
    int count = 0 ;
    char[][] grades ;
    String[] names ;


    Student(int n,int sub){ 
        this.n = n ;
        this.sub = sub;
        grades = new char[n][sub] ;
        names = new String[n] ;  
    }
    public void addStudentMarks(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the name of the student:");
        names[count] = sc.next() ;
        System.out.println("Enter the grades of the student based on the marks:");
        System.out.println("1. Marks >= 90 : O");
        System.out.println("2. Marks 80 <= x < 90 : A");
        System.out.println("3. Marks 70 <= x < 80 : B");
        System.out.println("4. Marks 60 <= x < 70 : C");
        System.out.println("5. Marks 50 <= x < 60 : D");
        System.out.println("6. Marks < 50 : F");

        for (int i =0 ; i < sub ; i++){
            System.out.printf("Enter the grade of the %s for subject%d: ",names[count],(i+1));
            grades[count][i] = Character.toUpperCase(sc.next().charAt(0)) ;
            
        }

        count ++ ;
        }
    
    public void calculateStats(){
        for (int i = 0 ; i < count ; i++){
            int total = 0 ;
            int highest = Integer.MIN_VALUE ;
            int lowest = Integer.MAX_VALUE ;
            for (int j =0 ; j < sub ; j++){
                int gradeValue = gradeToNumber(grades[i][j]);
                total += gradeValue ;
                if (gradeValue > highest) highest = gradeValue ;
                if(gradeValue < lowest) lowest = gradeValue ;
            }
            int average = total / sub ;
            

            System.out.printf("name :  %s:\n", names[i]);
            System.out.printf("Average Grade: %s \n", numberToGrade(average));
            System.out.println("Highest Grade: " + numberToGrade(highest));
            System.out.println("Lowest Grade: " + numberToGrade(lowest));
            System.out.println();
        }
    }
    public void display(){
        for (int i =0; i < n ; i++){
            System.out.printf("Student Name: %s\n", names[i]);
            System.out.println("Student grades:");
            for(int j=0; j < sub; j++){
                System.out.println(grades[i][j] + "");
                
            }
            System.out.println();
        }
    }
    
    private int gradeToNumber(char grade) {
        switch (grade) {
            case 'O':
                return 90;
            case 'A':
                return 80;
            case 'B':
                return 70;
            case 'C':
                return 60;
            case 'D':
                return 50;
            case 'F':
                return 40;
            default:
                return 0;
 
            } 
        }
            
    private char numberToGrade(int number) {
        if (number >= 90) return 'O';
        if (number >= 80) return 'A';
        if (number >= 70) return 'B';
        if (number >= 60) return 'C';
        if (number >= 50) return 'D';
        return 'F';
        
    }
}
class Main{
    public static void main(String args[]){
          Scanner sc = new Scanner(System.in) ;
          System.out.println("Enter the no of students:");
          int n = sc.nextInt() ;
          System.out.println("Enter the number of subjects:");
          int sub = sc.nextInt() ;
          Student s = new Student(n,sub);
          for (int i =0 ; i < n;i++){
              s.addStudentMarks();
          }
          
          s.display();
          s.calculateStats();
    }
}