/**
 * Copyright 2013 deib-polimi
 * Contact: deib-polimi <marco.miglierina@polimi.it>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package it.polimi.test.generator.entities;

import it.polimi.test.generator.RandomUtils;
import it.polimi.test.generator.Randomizable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@ToString(exclude = "projects")
@EqualsAndHashCode(exclude = "projects")
@NoArgsConstructor
@Entity
@Table(schema = "gae@pu")
public class EmployeeMTM implements Randomizable<EmployeeMTM> {

    @Id
    @Column(name = "EMPLOYEE_ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SALARY")
    private Long salary;

    @ManyToMany
    @JoinTable(name = "EMPLOYEE_PROJECT", joinColumns = {@JoinColumn(name = "EMPLOYEE_ID")}, inverseJoinColumns = {@JoinColumn(name = "PROJECT_ID")})
    private List<ProjectMTM> projects;

    @Override
    public EmployeeMTM randomize(Object dependency) {
        setName(RandomUtils.randomString());
        setSalary(RandomUtils.randomLong());
        setProjects((List<ProjectMTM>) dependency);
        return this;
    }
}
