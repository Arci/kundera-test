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
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(schema = "gae@pu")
public class Phone implements Randomizable<Phone> {

    @Id
    @Column(name = "PHONE_ID")
    private String id;

    @Column(name = "NUMBER")
    private Long number;

    @Override
    public Phone randomize(Object dependency) {
        setNumber(RandomUtils.randomLong());
        return this;
    }
}
